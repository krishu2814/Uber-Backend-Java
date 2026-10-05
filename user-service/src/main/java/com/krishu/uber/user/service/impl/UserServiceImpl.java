package com.krishu.uber.user.service.impl;

import com.krishu.uber.user.dto.AuthResponse;
import com.krishu.uber.user.dto.LoginRequest;
import com.krishu.uber.user.dto.RiderDto;
import com.krishu.uber.user.dto.SignupRequest;
import com.krishu.uber.user.dto.UserDto;
import com.krishu.uber.user.entity.Rider;
import com.krishu.uber.user.entity.User;
import com.krishu.uber.user.enums.Role;
import com.krishu.uber.user.exception.BadRequestException;
import com.krishu.uber.user.exception.ResourceNotFoundException;
import com.krishu.uber.user.repository.RiderRepository;
import com.krishu.uber.user.repository.UserRepository;
import com.krishu.uber.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Implementation of user business logic
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RiderRepository riderRepository;

    // Constructor injection
    public UserServiceImpl(UserRepository userRepository, RiderRepository riderRepository) {
        this.userRepository = userRepository;
        this.riderRepository = riderRepository;
    }

    @Override
    @Transactional
    public AuthResponse registerRider(SignupRequest request) {
        // Step 1: Check if email is already taken
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An account with email " + request.getEmail() + " already exists");
        }

        // Step 2: Check if phone is already registered
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("An account with phone number " + request.getPhone() + " already exists");
        }

        // Step 3: Create and save the base user record
        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                Role.RIDER
        );
        User savedUser = userRepository.save(user);

        // Step 4: Create customer rider profile
        Rider rider = new Rider(savedUser, 5.0);
        Rider savedRider = riderRepository.save(rider);

        return new AuthResponse(
                savedUser.getId(),
                savedRider.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole(),
                "Rider registered successfully"
        );
    }

    @Override
    @Transactional
    public AuthResponse registerDriverUser(SignupRequest request) {
        // Step 1: Check if email is already taken
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("An account with email " + request.getEmail() + " already exists");
        }

        // Step 2: Check if phone is already registered
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new BadRequestException("An account with phone number " + request.getPhone() + " already exists");
        }

        // Step 3: Create base user with DRIVER role
        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhone(),
                Role.DRIVER
        );
        User savedUser = userRepository.save(user);

        return new AuthResponse(
                savedUser.getId(),
                null,
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getPhone(),
                savedUser.getRole(),
                "Driver user account created successfully"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // Step 1: Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("No account found with email: " + request.getEmail()));

        // Step 2: Check password
        if (!user.getPassword().equals(request.getPassword())) {
            throw new BadRequestException("Invalid email or password");
        }

        // Step 3: If user is a rider, retrieve their rider profile ID
        Long riderId = null;
        if (user.getRole() == Role.RIDER) {
            riderId = riderRepository.findByUserId(user.getId())
                    .map(Rider::getId)
                    .orElse(null);
        }

        return new AuthResponse(
                user.getId(),
                riderId,
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                "Login successful"
        );
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RiderDto getRiderByUserId(Long userId) {
        Rider rider = riderRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Rider profile not found for user ID: " + userId));

        return new RiderDto(
                rider.getId(),
                rider.getUser().getId(),
                rider.getUser().getName(),
                rider.getUser().getEmail(),
                rider.getUser().getPhone(),
                rider.getRating()
        );
    }
}
