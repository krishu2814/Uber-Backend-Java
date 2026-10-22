package com.krishu.uber.ride.client;

import com.krishu.uber.ride.dto.SettleRideRequest;
import com.krishu.uber.ride.dto.SettlementResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// OpenFeign client to communicate with payment-service for ride fare settlements
@FeignClient(name = "payment-service")
public interface PaymentFeignClient {

    // Settle ride fare between rider and driver digital wallets
    @PostMapping("/api/wallets/settle-ride")
    SettlementResponseDto settleRide(@RequestBody SettleRideRequest request);
}
