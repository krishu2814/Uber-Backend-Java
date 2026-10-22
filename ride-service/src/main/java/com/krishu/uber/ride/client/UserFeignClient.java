package com.krishu.uber.ride.client;

import com.krishu.uber.ride.dto.RiderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// OpenFeign client to communicate with user-service
@FeignClient(name = "user-service")
public interface UserFeignClient {

    // Get rider profile by rider ID
    @GetMapping("/api/auth/riders/{riderId}")
    RiderDto getRiderById(@PathVariable("riderId") Long riderId);

    // Get rider profile by user ID
    @GetMapping("/api/auth/riders/user/{userId}")
    RiderDto getRiderByUserId(@PathVariable("userId") Long userId);
}
