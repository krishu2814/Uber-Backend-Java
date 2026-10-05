package com.krishu.uber.driver.service.impl;

import com.krishu.uber.driver.dto.DriverRegistrationRequest;
import com.krishu.uber.driver.dto.DriverResponseDto;
import com.krishu.uber.driver.dto.LocationUpdateRequest;
import com.krishu.uber.driver.dto.NearbyDriverDto;
import com.krishu.uber.driver.entity.Driver;
import com.krishu.uber.driver.enums.DriverStatus;
import com.krishu.uber.driver.exception.BadRequestException;
import com.krishu.uber.driver.exception.ResourceNotFoundException;
import com.krishu.uber.driver.repository.DriverRepository;
import com.krishu.uber.driver.service.DriverService;
import com.krishu.uber.driver.util.LocationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// Implementation of driver business logic
@Service
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;

    // Standard constructor injection
    public DriverServiceImpl(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    @Override
    @Transactional
    public DriverResponseDto registerDriver(DriverRegistrationRequest request) {
        // Step 1: Ensure user does not already have an existing driver profile
        if (driverRepository.existsByUserId(request.getUserId())) {
            throw new BadRequestException("Driver profile already exists for user ID: " + request.getUserId());
        }

        // Step 2: Ensure vehicle license plate number is unique
        if (driverRepository.existsByVehicleNumber(request.getVehicleNumber())) {
            throw new BadRequestException("Vehicle number " + request.getVehicleNumber() + " is already registered with another driver");
        }

        // Step 3: Create and persist new Driver entity
        Driver driver = new Driver(
                request.getUserId(),
                request.getVehicleNumber().trim().toUpperCase(),
                request.getVehicleModel().trim(),
                request.getLatitude(),
                request.getLongitude()
        );

        Driver savedDriver = driverRepository.save(driver);

        return mapToResponseDto(savedDriver, "Driver registered successfully and is now AVAILABLE");
    }

    @Override
    @Transactional
    public DriverResponseDto updateLocation(Long driverId, LocationUpdateRequest request) {
        Driver driver = findDriverOrThrow(driverId);

        // Update GPS coordinates
        driver.setCurrentLatitude(request.getLatitude());
        driver.setCurrentLongitude(request.getLongitude());

        Driver updatedDriver = driverRepository.save(driver);

        return mapToResponseDto(updatedDriver, "Driver location updated successfully");
    }

    @Override
    @Transactional
    public DriverResponseDto updateStatus(Long driverId, DriverStatus status) {
        Driver driver = findDriverOrThrow(driverId);

        driver.setStatus(status);
        Driver updatedDriver = driverRepository.save(driver);

        return mapToResponseDto(updatedDriver, "Driver status changed to " + status);
    }

    @Override
    @Transactional(readOnly = true)
    public DriverResponseDto getDriverById(Long driverId) {
        Driver driver = findDriverOrThrow(driverId);
        return mapToResponseDto(driver, "Driver profile retrieved successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public DriverResponseDto getDriverByUserId(Long userId) {
        Driver driver = driverRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No driver profile found for user ID: " + userId));

        return mapToResponseDto(driver, "Driver profile retrieved successfully");
    }

    @Override
    @Transactional(readOnly = true)
    public List<NearbyDriverDto> findNearbyAvailableDrivers(double latitude, double longitude, double radiusKm) {
        // Step 1: Query all drivers who are currently AVAILABLE
        List<Driver> availableDrivers = driverRepository.findByStatus(DriverStatus.AVAILABLE);

        // Step 2: Filter drivers with valid GPS coordinates, compute Haversine distance, and sort ascending
        return availableDrivers.stream()
                .filter(driver -> driver.getCurrentLatitude() != null && driver.getCurrentLongitude() != null)
                .map(driver -> {
                    double distance = LocationUtil.calculateDistance(
                            latitude,
                            longitude,
                            driver.getCurrentLatitude(),
                            driver.getCurrentLongitude()
                    );
                    return new NearbyDriverDto(
                            driver.getId(),
                            driver.getUserId(),
                            driver.getVehicleNumber(),
                            driver.getVehicleModel(),
                            driver.getCurrentLatitude(),
                            driver.getCurrentLongitude(),
                            distance,
                            driver.getRating()
                    );
                })
                .filter(dto -> dto.getDistanceKm() <= radiusKm)
                .sorted(Comparator.comparingDouble(NearbyDriverDto::getDistanceKm))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DriverResponseDto> getAllAvailableDrivers() {
        return driverRepository.findByStatus(DriverStatus.AVAILABLE)
                .stream()
                .map(driver -> mapToResponseDto(driver, null))
                .collect(Collectors.toList());
    }

    // Helper method to look up a driver or throw 404
    private Driver findDriverOrThrow(Long driverId) {
        return driverRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found with ID: " + driverId));
    }

    // Helper method to map Driver entity to DriverResponseDto
    private DriverResponseDto mapToResponseDto(Driver driver, String message) {
        return new DriverResponseDto(
                driver.getId(),
                driver.getUserId(),
                driver.getVehicleNumber(),
                driver.getVehicleModel(),
                driver.getStatus(),
                driver.getCurrentLatitude(),
                driver.getCurrentLongitude(),
                driver.getRating(),
                message
        );
    }
}
