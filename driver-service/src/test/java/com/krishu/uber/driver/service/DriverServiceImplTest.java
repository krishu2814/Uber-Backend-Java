package com.krishu.uber.driver.service;

import com.krishu.uber.driver.dto.DriverRegistrationRequest;
import com.krishu.uber.driver.dto.DriverResponseDto;
import com.krishu.uber.driver.dto.LocationUpdateRequest;
import com.krishu.uber.driver.dto.NearbyDriverDto;
import com.krishu.uber.driver.entity.Driver;
import com.krishu.uber.driver.enums.DriverStatus;
import com.krishu.uber.driver.repository.DriverRepository;
import com.krishu.uber.driver.service.impl.DriverServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceImplTest {

    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver sampleDriver;

    @BeforeEach
    void setUp() {
        sampleDriver = new Driver(2L, "DL-01-AB-1234", "Honda City", 28.6139, 77.2090);
        sampleDriver.setId(1L);
        sampleDriver.setStatus(DriverStatus.AVAILABLE);
    }

    @Test
    void testRegisterDriver_Success() {
        when(driverRepository.existsByUserId(2L)).thenReturn(false);
        when(driverRepository.existsByVehicleNumber("DL-01-AB-1234")).thenReturn(false);
        when(driverRepository.save(any(Driver.class))).thenAnswer(inv -> {
            Driver d = inv.getArgument(0);
            d.setId(1L);
            return d;
        });

        DriverRegistrationRequest req = new DriverRegistrationRequest(2L, "DL-01-AB-1234", "Honda City", 28.6139, 77.2090);
        DriverResponseDto response = driverService.registerDriver(req);

        assertNotNull(response);
        assertEquals(DriverStatus.AVAILABLE, response.getStatus());
        assertEquals("DL-01-AB-1234", response.getVehicleNumber());
    }

    @Test
    void testUpdateLocation_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(sampleDriver));
        when(driverRepository.save(any(Driver.class))).thenReturn(sampleDriver);

        LocationUpdateRequest req = new LocationUpdateRequest(28.6200, 77.2150);
        DriverResponseDto response = driverService.updateLocation(1L, req);

        assertNotNull(response);
        assertEquals(28.6200, response.getLatitude());
        assertEquals(77.2150, response.getLongitude());
    }

    @Test
    void testFindNearbyAvailableDrivers_Haversine() {
        when(driverRepository.findByStatus(DriverStatus.AVAILABLE)).thenReturn(List.of(sampleDriver));

        // Rider at 28.6140, 77.2095 (less than 1 km away)
        List<NearbyDriverDto> nearby = driverService.findNearbyAvailableDrivers(28.6140, 77.2095, 5.0);

        assertNotNull(nearby);
        assertEquals(1, nearby.size());
        assertTrue(nearby.get(0).getDistanceKm() < 1.0);
    }
}
