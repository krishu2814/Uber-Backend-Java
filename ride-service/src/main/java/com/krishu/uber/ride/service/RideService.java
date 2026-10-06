package com.krishu.uber.ride.service;

import com.krishu.uber.ride.dto.RideRequestDto;
import com.krishu.uber.ride.dto.RideResponseDto;

import java.util.List;

// Service interface orchestrating ride booking, dispatch, OTP verification, and trip lifecycle
public interface RideService {

    // Rider requests a new ride, calculates distance & fare, and generates a secure 4-digit OTP
    RideResponseDto requestRide(RideRequestDto request);

    // Driver accepts an available ride with atomic concurrency check
    RideResponseDto acceptRide(Long rideId, Long driverId);

    // Driver marks arrival at the rider's pickup location
    RideResponseDto driverArrived(Long rideId, Long driverId);

    // Driver verifies rider's 4-digit OTP and begins the trip
    RideResponseDto startRide(Long rideId, Long driverId, String otp);

    // Driver concludes the trip upon arriving at the destination
    RideResponseDto endRide(Long rideId, Long driverId);

    // Cancel a ride (either by rider or driver before commencement)
    RideResponseDto cancelRide(Long rideId, String reason);

    // Get details of a single ride by ID
    RideResponseDto getRideById(Long rideId);

    // Get trip history for a specific rider
    List<RideResponseDto> getRidesByRiderId(Long riderId);

    // Get trip history for a specific driver
    List<RideResponseDto> getRidesByDriverId(Long driverId);

    // Get all rides waiting in the dispatch queue with status REQUESTED
    List<RideResponseDto> getPendingRides();
}
