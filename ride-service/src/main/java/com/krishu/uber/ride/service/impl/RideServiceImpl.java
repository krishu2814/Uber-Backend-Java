package com.krishu.uber.ride.service.impl;

import com.krishu.uber.ride.client.DriverFeignClient;
import com.krishu.uber.ride.dto.RideRequestDto;
import com.krishu.uber.ride.dto.RideResponseDto;
import com.krishu.uber.ride.entity.Ride;
import com.krishu.uber.ride.enums.RideStatus;
import com.krishu.uber.ride.exception.BadRequestException;
import com.krishu.uber.ride.exception.ResourceNotFoundException;
import com.krishu.uber.ride.repository.RideRepository;
import com.krishu.uber.ride.service.RideService;
import com.krishu.uber.ride.util.FareCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

// Core implementation of Ride booking, dispatching, OTP verification, and trip state management
@Service
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final DriverFeignClient driverFeignClient;
    private final Random random = new Random();

    // Standard constructor injection
    public RideServiceImpl(RideRepository rideRepository, DriverFeignClient driverFeignClient) {
        this.rideRepository = rideRepository;
        this.driverFeignClient = driverFeignClient;
    }

    @Override
    @Transactional
    public RideResponseDto requestRide(RideRequestDto request) {
        // Step 1: Calculate route distance using the Haversine formula
        double distanceKm = FareCalculator.calculateDistance(
                request.getPickupLatitude(),
                request.getPickupLongitude(),
                request.getDropoffLatitude(),
                request.getDropoffLongitude()
        );

        // Step 2: Compute estimated trip fare
        double surge = request.getSurgeMultiplier() != null ? request.getSurgeMultiplier() : 1.0;
        double fare = FareCalculator.calculateFare(distanceKm, surge);

        // Step 3: Generate a secure 4-digit numeric OTP for rider authentication (e.g., "7412")
        String otp = String.format("%04d", random.nextInt(10000));

        // Step 4: Create and persist new Ride entity in REQUESTED status
        Ride ride = new Ride(
                request.getRiderId(),
                request.getPickupAddress(),
                request.getDropoffAddress(),
                request.getPickupLatitude(),
                request.getPickupLongitude(),
                request.getDropoffLatitude(),
                request.getDropoffLongitude(),
                distanceKm,
                fare,
                otp
        );

        Ride savedRide = rideRepository.save(ride);

        return mapToDto(savedRide, "Ride requested successfully! Waiting for a nearby driver to accept.");
    }

    @Override
    @Transactional
    public synchronized RideResponseDto acceptRide(Long rideId, Long driverId) {
        // Step 1: Find the requested ride
        Ride ride = findRideOrThrow(rideId);

        // Step 2: Atomic check to ensure only the first driver who clicks 'Accept' gets the ride
        if (ride.getStatus() != RideStatus.REQUESTED) {
            throw new BadRequestException("Ride request #" + rideId + " is no longer available. Current status: " + ride.getStatus());
        }

        // Step 3: Assign driver and update status to ACCEPTED
        ride.setDriverId(driverId);
        ride.setStatus(RideStatus.ACCEPTED);
        ride.setAcceptedAt(LocalDateTime.now());
        Ride updatedRide = rideRepository.save(ride);

        // Step 4: Inter-service call to mark the driver as BUSY in driver-service
        try {
            driverFeignClient.updateDriverStatus(driverId, "BUSY");
        } catch (Exception e) {
            // Log warning if driver service is temporarily unreachable; ride state remains preserved
            System.err.println("Warning: Failed to update driver status in driver-service: " + e.getMessage());
        }

        return mapToDto(updatedRide, "Ride accepted successfully! Driver is on the way to the pickup location.");
    }

    @Override
    @Transactional
    public RideResponseDto driverArrived(Long rideId, Long driverId) {
        Ride ride = findRideOrThrow(rideId);

        // Validate assigned driver
        validateAssignedDriver(ride, driverId);

        if (ride.getStatus() != RideStatus.ACCEPTED) {
            throw new BadRequestException("Cannot mark arrival for ride with status: " + ride.getStatus());
        }

        ride.setStatus(RideStatus.ARRIVED);
        Ride updatedRide = rideRepository.save(ride);

        return mapToDto(updatedRide, "Driver has arrived at the pickup location. Waiting for rider to board.");
    }

    @Override
    @Transactional
    public RideResponseDto startRide(Long rideId, Long driverId, String otp) {
        Ride ride = findRideOrThrow(rideId);

        // Validate assigned driver
        validateAssignedDriver(ride, driverId);

        // Ensure ride is in ready state (either ACCEPTED or ARRIVED)
        if (ride.getStatus() != RideStatus.ARRIVED && ride.getStatus() != RideStatus.ACCEPTED) {
            throw new BadRequestException("Cannot start ride with current status: " + ride.getStatus());
        }

        // Step 1: Verify the 4-digit OTP provided by rider
        if (otp == null || !ride.getOtp().equals(otp.trim())) {
            throw new BadRequestException("Invalid OTP provided! Please ask the rider for their 4-digit verification code.");
        }

        // Step 2: Transition ride to STARTED status
        ride.setStatus(RideStatus.STARTED);
        ride.setStartedAt(LocalDateTime.now());
        Ride updatedRide = rideRepository.save(ride);

        return mapToDto(updatedRide, "OTP verified successfully! Trip has started.");
    }

    @Override
    @Transactional
    public RideResponseDto endRide(Long rideId, Long driverId) {
        Ride ride = findRideOrThrow(rideId);

        // Validate assigned driver
        validateAssignedDriver(ride, driverId);

        if (ride.getStatus() != RideStatus.STARTED) {
            throw new BadRequestException("Cannot end ride that has not started. Current status: " + ride.getStatus());
        }

        // Step 1: Conclude the trip
        ride.setStatus(RideStatus.COMPLETED);
        ride.setCompletedAt(LocalDateTime.now());
        Ride updatedRide = rideRepository.save(ride);

        // Step 2: Inter-service call to release driver back to AVAILABLE state
        try {
            driverFeignClient.updateDriverStatus(driverId, "AVAILABLE");
        } catch (Exception e) {
            System.err.println("Warning: Failed to reset driver status in driver-service: " + e.getMessage());
        }

        return mapToDto(updatedRide, "Trip completed safely! Total fare is ₹" + updatedRide.getFare());
    }

    @Override
    @Transactional
    public RideResponseDto cancelRide(Long rideId, String reason) {
        Ride ride = findRideOrThrow(rideId);

        // Cannot cancel already completed or in-progress trips
        if (ride.getStatus() == RideStatus.STARTED || ride.getStatus() == RideStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel an in-progress or completed ride.");
        }

        if (ride.getStatus() == RideStatus.CANCELLED) {
            throw new BadRequestException("Ride is already cancelled.");
        }

        // Free up the driver if one was assigned
        if (ride.getDriverId() != null) {
            try {
                driverFeignClient.updateDriverStatus(ride.getDriverId(), "AVAILABLE");
            } catch (Exception e) {
                System.err.println("Warning: Failed to reset driver status: " + e.getMessage());
            }
        }

        ride.setStatus(RideStatus.CANCELLED);
        Ride updatedRide = rideRepository.save(ride);

        String message = "Ride has been cancelled.";
        if (reason != null && !reason.trim().isEmpty()) {
            message += " Reason: " + reason;
        }

        return mapToDto(updatedRide, message);
    }

    @Override
    @Transactional(readOnly = true)
    public RideResponseDto getRideById(Long rideId) {
        Ride ride = findRideOrThrow(rideId);
        return mapToDto(ride, "Ride details retrieved successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponseDto> getRidesByRiderId(Long riderId) {
        return rideRepository.findByRiderIdOrderByCreatedAtDesc(riderId)
                .stream()
                .map(ride -> mapToDto(ride, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponseDto> getRidesByDriverId(Long driverId) {
        return rideRepository.findByDriverIdOrderByCreatedAtDesc(driverId)
                .stream()
                .map(ride -> mapToDto(ride, null))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RideResponseDto> getPendingRides() {
        return rideRepository.findByStatusOrderByCreatedAtAsc(RideStatus.REQUESTED)
                .stream()
                .map(ride -> mapToDto(ride, null))
                .collect(Collectors.toList());
    }

    // Helper method to look up a ride or throw 404
    private Ride findRideOrThrow(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with ID: " + rideId));
    }

    // Helper method to verify the operating driver matches the assigned driver
    private void validateAssignedDriver(Ride ride, Long driverId) {
        if (ride.getDriverId() == null || !ride.getDriverId().equals(driverId)) {
            throw new BadRequestException("Driver #" + driverId + " is not authorized for ride #" + ride.getId());
        }
    }

    // Helper method to map Ride entity to RideResponseDto
    private RideResponseDto mapToDto(Ride ride, String message) {
        return new RideResponseDto(
                ride.getId(),
                ride.getRiderId(),
                ride.getDriverId(),
                ride.getPickupAddress(),
                ride.getDropoffAddress(),
                ride.getPickupLatitude(),
                ride.getPickupLongitude(),
                ride.getDropoffLatitude(),
                ride.getDropoffLongitude(),
                ride.getDistanceKm(),
                ride.getFare(),
                ride.getOtp(),
                ride.getStatus(),
                ride.getPaymentStatus(),
                ride.getCreatedAt(),
                ride.getAcceptedAt(),
                ride.getStartedAt(),
                ride.getCompletedAt(),
                message
        );
    }
}
