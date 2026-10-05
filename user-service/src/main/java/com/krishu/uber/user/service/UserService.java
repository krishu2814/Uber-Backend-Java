package com.krishu.uber.user.service;

import com.krishu.uber.user.dto.AuthResponse;
import com.krishu.uber.user.dto.LoginRequest;
import com.krishu.uber.user.dto.RiderDto;
import com.krishu.uber.user.dto.SignupRequest;
import com.krishu.uber.user.dto.UserDto;

// Business service for user authentication and profile management
public interface UserService {

    // Register a new customer as a Rider
    AuthResponse registerRider(SignupRequest request);

    // Register a new user as a Driver account
    AuthResponse registerDriverUser(SignupRequest request);

    // Authenticate user with email and password
    AuthResponse login(LoginRequest request);

    // Get user details by user ID
    UserDto getUserById(Long userId);

    // Get rider profile by user ID
    RiderDto getRiderByUserId(Long userId);
}
