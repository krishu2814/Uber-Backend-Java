package com.krishu.uber.ride.repository;

import com.krishu.uber.ride.entity.Ride;
import com.krishu.uber.ride.enums.RideStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// Spring Data JPA repository for Ride entities
@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {

    // Retrieve all rides booked by a rider, ordered with the latest first
    List<Ride> findByRiderIdOrderByCreatedAtDesc(Long riderId);

    // Retrieve all rides serviced by a driver, ordered with the latest first
    List<Ride> findByDriverIdOrderByCreatedAtDesc(Long driverId);

    // Retrieve all rides matching a given status (e.g., REQUESTED) ordered by oldest first (FIFO queue)
    List<Ride> findByStatusOrderByCreatedAtAsc(RideStatus status);
}
