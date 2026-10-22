package com.krishu.uber.user.service;

import com.krishu.uber.user.client.WalletFeignClient;
import com.krishu.uber.user.dto.AuthResponse;
import com.krishu.uber.user.dto.LoginRequest;
import com.krishu.uber.user.dto.RiderDto;
import com.krishu.uber.user.dto.SignupRequest;
import com.krishu.uber.user.entity.Rider;
import com.krishu.uber.user.entity.User;
import com.krishu.uber.user.enums.Role;
import com.krishu.uber.user.exception.BadRequestException;
import com.krishu.uber.user.repository.RiderRepository;
import com.krishu.uber.user.repository.UserRepository;
import com.krishu.uber.user.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RiderRepository riderRepository;

    @Mock
    private WalletFeignClient walletFeignClient;

    @InjectMocks
    private UserServiceImpl userService;

    private User sampleUser;
    private Rider sampleRider;

    @BeforeEach
    void setUp() {
        sampleUser = new User("Rahul Sharma", "rahul@example.com", "password123", "9876543210", Role.RIDER);
        sampleUser.setId(1L);

        sampleRider = new Rider(sampleUser, 5.0);
        sampleRider.setId(10L);
    }

    @Test
    void testRegisterRider_Success_TriggersWalletCreation() {
        when(userRepository.existsByEmail("rahul@example.com")).thenReturn(false);
        when(userRepository.existsByPhone("9876543210")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        when(riderRepository.save(any(Rider.class))).thenReturn(sampleRider);
        when(walletFeignClient.createWallet(1L)).thenReturn(ResponseEntity.ok().build());

        SignupRequest req = new SignupRequest("Rahul Sharma", "rahul@example.com", "password123", "9876543210", Role.RIDER);
        AuthResponse response = userService.registerRider(req);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(10L, response.getRiderId());
        assertEquals("Rahul Sharma", response.getName());
        verify(walletFeignClient, times(1)).createWallet(1L);
    }

    @Test
    void testLogin_Success() {
        when(userRepository.findByEmail("rahul@example.com")).thenReturn(Optional.of(sampleUser));
        when(riderRepository.findByUserId(1L)).thenReturn(Optional.of(sampleRider));

        LoginRequest req = new LoginRequest("rahul@example.com", "password123");
        AuthResponse response = userService.login(req);

        assertNotNull(response);
        assertEquals(1L, response.getUserId());
        assertEquals(10L, response.getRiderId());
    }

    @Test
    void testLogin_InvalidPassword_ThrowsBadRequest() {
        when(userRepository.findByEmail("rahul@example.com")).thenReturn(Optional.of(sampleUser));

        LoginRequest req = new LoginRequest("rahul@example.com", "wrongpassword");
        assertThrows(BadRequestException.class, () -> userService.login(req));
    }

    @Test
    void testGetRiderById_Success() {
        when(riderRepository.findById(10L)).thenReturn(Optional.of(sampleRider));

        RiderDto rider = userService.getRiderById(10L);

        assertNotNull(rider);
        assertEquals(10L, rider.getRiderId());
        assertEquals(1L, rider.getUserId());
        assertEquals("Rahul Sharma", rider.getName());
    }
}
