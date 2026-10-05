package com.krishu.uber.user.repository;

import com.krishu.uber.user.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repository interface for database operations on riders table
@Repository
public interface RiderRepository extends JpaRepository<Rider, Long> {

    // Find rider profile using user ID
    Optional<Rider> findByUserId(Long userId);

    // Find rider profile using email
    Optional<Rider> findByUserEmail(String email);
}
