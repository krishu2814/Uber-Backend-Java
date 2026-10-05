package com.krishu.uber.user.repository;

import com.krishu.uber.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repository interface for database operations on users table
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email for login authentication
    Optional<User> findByEmail(String email);

    // Check if an email already exists during registration
    boolean existsByEmail(String email);

    // Check if a phone number already exists during registration
    boolean existsByPhone(String phone);
}
