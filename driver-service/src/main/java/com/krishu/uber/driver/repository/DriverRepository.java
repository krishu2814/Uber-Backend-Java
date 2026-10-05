package com.krishu.uber.driver.repository;

import com.krishu.uber.driver.entity.Driver;
import com.krishu.uber.driver.enums.DriverStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Spring Data JPA repository for Driver operations
@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    // Find driver by associated user ID
    Optional<Driver> findByUserId(Long userId);

    // Find driver by vehicle plate number
    Optional<Driver> findByVehicleNumber(String vehicleNumber);

    // Find all drivers matching a specific availability status (e.g. AVAILABLE)
    List<Driver> findByStatus(DriverStatus status);

    // Check if a vehicle number is already registered
    boolean existsByVehicleNumber(String vehicleNumber);

    // Check if a user already has a driver profile
    boolean existsByUserId(Long userId);
}
