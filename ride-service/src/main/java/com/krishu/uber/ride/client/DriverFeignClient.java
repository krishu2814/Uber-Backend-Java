package com.krishu.uber.ride.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

// OpenFeign declarative REST client to communicate with driver-service via Eureka service discovery
@FeignClient(name = "driver-service")
public interface DriverFeignClient {

    // Update driver availability status (AVAILABLE, BUSY, OFFLINE)
    @PutMapping("/api/drivers/{id}/status")
    ResponseEntity<Object> updateDriverStatus(
            @PathVariable("id") Long driverId,
            @RequestParam("status") String status
    );

    // Get driver details by driver ID
    @GetMapping("/api/drivers/{id}")
    ResponseEntity<Object> getDriverById(@PathVariable("id") Long driverId);
}
