package com.krishu.uber.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// OpenFeign declarative REST client to communicate with payment-service via Eureka
@FeignClient(name = "payment-service")
public interface WalletFeignClient {

    // Automatically create digital wallet with ₹100 welcome bonus
    @PostMapping("/api/wallets/create")
    ResponseEntity<Object> createWallet(@RequestParam("userId") Long userId);
}
