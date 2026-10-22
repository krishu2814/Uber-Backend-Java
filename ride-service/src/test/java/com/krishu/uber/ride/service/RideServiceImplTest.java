package com.krishu.uber.ride.service;

import com.krishu.uber.ride.client.DriverFeignClient;
import com.krishu.uber.ride.client.PaymentFeignClient;
import com.krishu.uber.ride.client.UserFeignClient;
import com.krishu.uber.ride.dto.*;
import com.krishu.uber.ride.entity.Ride;
import com.krishu.uber.ride.enums.PaymentStatus;
import com.krishu.uber.ride.enums.RideStatus;
import com.krishu.uber.ride.exception.BadRequestException;
import com.krishu.uber.ride.repository.RideRepository;
import com.krishu.uber.ride.service.impl.RideServiceImpl;
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
class RideServiceImplTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private DriverFeignClient driverFeignClient;

    @Mock
    private PaymentFeignClient paymentFeignClient;

    @Mock
    private UserFeignClient userFeignClient;

    @InjectMocks
    private RideServiceImpl rideService;

    private Ride sampleRide;

    @BeforeEach
    void setUp() {
        sampleRide = new Ride(1L, "Connaught Place", "India Gate", 28.6315, 77.2167, 28.6129, 77.2295, 2.41, 78.92, "1234");
        sampleRide.setId(100L);
    }

    @Test
    void testRequestRide_Success() {
        when(rideRepository.save(any(Ride.class))).thenAnswer(inv -> {
            Ride r = inv.getArgument(0);
            r.setId(100L);
            return r;
        });

        RideRequestDto req = new RideRequestDto(1L, "Connaught Place", "India Gate", 28.6315, 77.2167, 28.6129, 77.2295, 1.0);
        RideResponseDto response = rideService.requestRide(req);

        assertNotNull(response);
        assertEquals(RideStatus.REQUESTED, response.getStatus());
        assertNotNull(response.getOtp());
        assertEquals(4, response.getOtp().length());
        assertTrue(response.getFare() > 0);
    }

    @Test
    void testAcceptRide_AtomicSuccess() {
        when(rideRepository.findById(100L)).thenReturn(Optional.of(sampleRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(sampleRide);
        when(driverFeignClient.updateDriverStatus(2L, "BUSY")).thenReturn(ResponseEntity.ok().build());

        RideResponseDto response = rideService.acceptRide(100L, 2L);

        assertNotNull(response);
        assertEquals(RideStatus.ACCEPTED, response.getStatus());
        assertEquals(2L, response.getDriverId());
        verify(driverFeignClient, times(1)).updateDriverStatus(2L, "BUSY");
    }

    @Test
    void testAcceptRide_AlreadyAccepted_ThrowsBadRequest() {
        sampleRide.setStatus(RideStatus.ACCEPTED);
        when(rideRepository.findById(100L)).thenReturn(Optional.of(sampleRide));

        assertThrows(BadRequestException.class, () -> rideService.acceptRide(100L, 2L));
    }

    @Test
    void testStartRide_OtpValid_Success() {
        sampleRide.setStatus(RideStatus.ACCEPTED);
        sampleRide.setDriverId(2L);
        when(rideRepository.findById(100L)).thenReturn(Optional.of(sampleRide));
        when(rideRepository.save(any(Ride.class))).thenReturn(sampleRide);

        RideResponseDto response = rideService.startRide(100L, 2L, "1234");

        assertNotNull(response);
        assertEquals(RideStatus.STARTED, response.getStatus());
    }

    @Test
    void testStartRide_InvalidOtp_ThrowsBadRequest() {
        sampleRide.setStatus(RideStatus.ACCEPTED);
        sampleRide.setDriverId(2L);
        when(rideRepository.findById(100L)).thenReturn(Optional.of(sampleRide));

        assertThrows(BadRequestException.class, () -> rideService.startRide(100L, 2L, "9999"));
    }

    @Test
    void testEndRide_SettlesPaymentViaFeign_Success() {
        sampleRide.setStatus(RideStatus.STARTED);
        sampleRide.setDriverId(2L);
        when(rideRepository.findById(100L)).thenReturn(Optional.of(sampleRide));
        when(rideRepository.save(any(Ride.class))).thenAnswer(inv -> inv.getArgument(0));

        when(driverFeignClient.updateDriverStatus(2L, "AVAILABLE")).thenReturn(ResponseEntity.ok().build());
        when(driverFeignClient.getDriverById(2L)).thenReturn(new DriverDto(2L, 20L, "DL-01", "City", "AVAILABLE", 0.0, 0.0, 5.0));
        when(userFeignClient.getRiderById(1L)).thenReturn(new RiderDto(1L, 10L, "Rahul", "rahul@test.com", "9876", 5.0));

        SettlementResponseDto settlement = new SettlementResponseDto(100L, 78.92, 10L, 100.0, 20L, 200.0, "SUCCESS", "Settled");
        when(paymentFeignClient.settleRide(any(SettleRideRequest.class))).thenReturn(settlement);

        RideResponseDto response = rideService.endRide(100L, 2L);

        assertNotNull(response);
        assertEquals(RideStatus.COMPLETED, response.getStatus());
        assertEquals(PaymentStatus.COMPLETED, response.getPaymentStatus());
        verify(paymentFeignClient, times(1)).settleRide(any(SettleRideRequest.class));
        verify(driverFeignClient, times(1)).updateDriverStatus(2L, "AVAILABLE");
    }
}
