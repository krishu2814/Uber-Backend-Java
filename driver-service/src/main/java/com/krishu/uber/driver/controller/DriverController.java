package com.krishu.uber.driver.controller;

import com.krishu.uber.driver.dto.DriverRegistrationRequest;
import com.krishu.uber.driver.dto.DriverResponseDto;
import com.krishu.uber.driver.dto.LocationUpdateRequest;
import com.krishu.uber.driver.dto.NearbyDriverDto;
import com.krishu.uber.driver.enums.DriverStatus;
import com.krishu.uber.driver.service.DriverService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST Controller exposing driver management, GPS location tracking, and nearby search APIs
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    // Standard constructor injection
    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Driver Service is UP and healthy on port 8082");
    }

    // Register a new driver profile with vehicle details
    @PostMapping("/register")
    public ResponseEntity<DriverResponseDto> registerDriver(@Valid @RequestBody DriverRegistrationRequest request) {
        DriverResponseDto response = driverService.registerDriver(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get driver details by driver ID
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDto> getDriverById(@PathVariable Long id) {
        DriverResponseDto response = driverService.getDriverById(id);
        return ResponseEntity.ok(response);
    }

    // Get driver details by associated user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<DriverResponseDto> getDriverByUserId(@PathVariable Long userId) {
        DriverResponseDto response = driverService.getDriverByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // Update real-time GPS location of a driver
    @PutMapping("/{id}/location")
    public ResponseEntity<DriverResponseDto> updateLocation(
            @PathVariable Long id,
            @Valid @RequestBody LocationUpdateRequest request) {
        DriverResponseDto response = driverService.updateLocation(id, request);
        return ResponseEntity.ok(response);
    }

    // Update driver availability status (AVAILABLE, BUSY, OFFLINE)
    // Supports query parameter e.g.: PUT /api/drivers/1/status?status=BUSY
    @PutMapping("/{id}/status")
    public ResponseEntity<DriverResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestParam DriverStatus status) {
        DriverResponseDto response = driverService.updateStatus(id, status);
        return ResponseEntity.ok(response);
    }

    // Find available drivers within a specified geographic radius (in km)
    // Defaults to 5.0 km radius if not specified
    @GetMapping("/nearby")
    public ResponseEntity<List<NearbyDriverDto>> getNearbyDrivers(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5.0") double radiusKm) {
        List<NearbyDriverDto> nearbyDrivers = driverService.findNearbyAvailableDrivers(latitude, longitude, radiusKm);
        return ResponseEntity.ok(nearbyDrivers);
    }

    // List all currently available drivers
    @GetMapping("/available")
    public ResponseEntity<List<DriverResponseDto>> getAllAvailableDrivers() {
        List<DriverResponseDto> drivers = driverService.getAllAvailableDrivers();
        return ResponseEntity.ok(drivers);
    }
}
