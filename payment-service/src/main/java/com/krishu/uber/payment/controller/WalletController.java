package com.krishu.uber.payment.controller;

import com.krishu.uber.payment.dto.*;
import com.krishu.uber.payment.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// REST Controller exposing endpoints for digital wallet operations, top-ups, settlements, and ledger audit
@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Payment & Digital Wallet Service is UP and running on port 8084");
    }

    // Create a new digital wallet for a user with ₹100 Welcome Bonus
    @PostMapping("/create")
    public ResponseEntity<WalletResponseDto> createWallet(@RequestParam Long userId) {
        WalletResponseDto response = walletService.createWallet(userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Fetch wallet details by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<WalletResponseDto> getWalletByUserId(@PathVariable Long userId) {
        WalletResponseDto response = walletService.getWalletByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // Fetch wallet details by internal wallet primary key
    @GetMapping("/{walletId}")
    public ResponseEntity<WalletResponseDto> getWalletById(@PathVariable Long walletId) {
        WalletResponseDto response = walletService.getWalletById(walletId);
        return ResponseEntity.ok(response);
    }

    // Add funds / top up wallet balance
    @PostMapping("/add-money")
    public ResponseEntity<WalletResponseDto> addMoney(@Valid @RequestBody AddMoneyRequest request) {
        WalletResponseDto response = walletService.addMoney(request);
        return ResponseEntity.ok(response);
    }

    // Settle ride fare between rider and driver wallets using double-entry accounting
    @PostMapping("/settle-ride")
    public ResponseEntity<SettlementResponseDto> settleRide(@Valid @RequestBody SettleRideRequest request) {
        SettlementResponseDto response = walletService.settleRide(request);
        return ResponseEntity.ok(response);
    }

    // Get complete transaction ledger history for a wallet ID
    @GetMapping("/{walletId}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(@PathVariable Long walletId) {
        List<TransactionDto> history = walletService.getTransactions(walletId);
        return ResponseEntity.ok(history);
    }

    // Get transaction ledger history by user ID
    @GetMapping("/user/{userId}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactionsByUserId(@PathVariable Long userId) {
        List<TransactionDto> history = walletService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(history);
    }
}
