package com.krishu.uber.ride.controller;

import com.krishu.uber.ride.dto.RideRequestDto;
import com.krishu.uber.ride.dto.RideResponseDto;
import com.krishu.uber.ride.service.RideService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST controller exposing ride booking, trip lifecycle progression, OTP checks, and dispatch history
@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    // Standard constructor injection
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Ride Service is UP and healthy on port 8083");
    }

    // Rider requests a new ride
    @PostMapping("/request")
    public ResponseEntity<RideResponseDto> requestRide(@Valid @RequestBody RideRequestDto request) {
        RideResponseDto response = rideService.requestRide(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Driver accepts an available ride request
    @PostMapping("/{id}/accept")
    public ResponseEntity<RideResponseDto> acceptRide(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        RideResponseDto response = rideService.acceptRide(id, driverId);
        return ResponseEntity.ok(response);
    }

    // Driver marks arrival at the pickup point
    @PostMapping("/{id}/arrive")
    public ResponseEntity<RideResponseDto> driverArrived(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        RideResponseDto response = rideService.driverArrived(id, driverId);
        return ResponseEntity.ok(response);
    }

    // Driver verifies rider's 4-digit OTP and begins the trip
    @PostMapping("/{id}/start")
    public ResponseEntity<RideResponseDto> startRide(
            @PathVariable Long id,
            @RequestParam Long driverId,
            @RequestParam String otp) {
        RideResponseDto response = rideService.startRide(id, driverId, otp);
        return ResponseEntity.ok(response);
    }

    // Driver concludes the trip upon arriving at destination
    @PostMapping("/{id}/end")
    public ResponseEntity<RideResponseDto> endRide(
            @PathVariable Long id,
            @RequestParam Long driverId) {
        RideResponseDto response = rideService.endRide(id, driverId);
        return ResponseEntity.ok(response);
    }

    // Cancel a ride (prior to trip commencement)
    @PostMapping("/{id}/cancel")
    public ResponseEntity<RideResponseDto> cancelRide(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        RideResponseDto response = rideService.cancelRide(id, reason);
        return ResponseEntity.ok(response);
    }

    // Retrieve details of a single ride
    @GetMapping("/{id}")
    public ResponseEntity<RideResponseDto> getRideById(@PathVariable Long id) {
        RideResponseDto response = rideService.getRideById(id);
        return ResponseEntity.ok(response);
    }

    // Get all rides booked by a specific rider
    @GetMapping("/rider/{riderId}")
    public ResponseEntity<List<RideResponseDto>> getRidesByRiderId(@PathVariable Long riderId) {
        List<RideResponseDto> rides = rideService.getRidesByRiderId(riderId);
        return ResponseEntity.ok(rides);
    }

    // Get all rides fulfilled by a specific driver
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<RideResponseDto>> getRidesByDriverId(@PathVariable Long driverId) {
        List<RideResponseDto> rides = rideService.getRidesByDriverId(driverId);
        return ResponseEntity.ok(rides);
    }

    // Get all pending rides waiting in the dispatch queue (status REQUESTED)
    @GetMapping("/pending")
    public ResponseEntity<List<RideResponseDto>> getPendingRides() {
        List<RideResponseDto> rides = rideService.getPendingRides();
        return ResponseEntity.ok(rides);
    }
}
