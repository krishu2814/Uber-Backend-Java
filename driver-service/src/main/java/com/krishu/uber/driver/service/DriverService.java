package com.krishu.uber.driver.service;

import com.krishu.uber.driver.dto.DriverRegistrationRequest;
import com.krishu.uber.driver.dto.DriverResponseDto;
import com.krishu.uber.driver.dto.LocationUpdateRequest;
import com.krishu.uber.driver.dto.NearbyDriverDto;
import com.krishu.uber.driver.enums.DriverStatus;

import java.util.List;

// Service interface declaring business operations for drivers
public interface DriverService {

    // Register a new driver profile with vehicle details
    DriverResponseDto registerDriver(DriverRegistrationRequest request);

    // Update real-time GPS coordinates of a driver
    DriverResponseDto updateLocation(Long driverId, LocationUpdateRequest request);

    // Update operational availability status (AVAILABLE, BUSY, OFFLINE)
    DriverResponseDto updateStatus(Long driverId, DriverStatus status);

    // Retrieve driver profile by driver ID
    DriverResponseDto getDriverById(Long driverId);

    // Retrieve driver profile by associated user ID
    DriverResponseDto getDriverByUserId(Long userId);

    // Find available drivers within a radius in kilometers, sorted by distance ascending
    List<NearbyDriverDto> findNearbyAvailableDrivers(double latitude, double longitude, double radiusKm);

    // Get all drivers that are currently AVAILABLE
    List<DriverResponseDto> getAllAvailableDrivers();
}
