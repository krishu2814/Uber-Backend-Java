package com.krishu.uber.user.controller;

import com.krishu.uber.user.dto.AuthResponse;
import com.krishu.uber.user.dto.LoginRequest;
import com.krishu.uber.user.dto.RiderDto;
import com.krishu.uber.user.dto.SignupRequest;
import com.krishu.uber.user.dto.UserDto;
import com.krishu.uber.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// REST controller exposing authentication and user profile endpoints
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    // Standard constructor injection
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("User Service is UP and healthy on port 8081");
    }

    // Register a new customer as a Rider
    @PostMapping("/register-rider")
    public ResponseEntity<AuthResponse> registerRider(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = userService.registerRider(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Register a new driver account
    @PostMapping("/register-driver")
    public ResponseEntity<AuthResponse> registerDriver(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = userService.registerDriverUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Log in with email and password
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = userService.login(request);
        return ResponseEntity.ok(response);
    }

    // Get user details by user ID
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Get rider profile by user ID
    @GetMapping("/riders/user/{userId}")
    public ResponseEntity<RiderDto> getRiderByUserId(@PathVariable Long userId) {
        RiderDto rider = userService.getRiderByUserId(userId);
        return ResponseEntity.ok(rider);
    }
}
