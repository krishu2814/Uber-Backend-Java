package com.krishu.uber.payment.dto;

import java.time.LocalDateTime;

// Response DTO containing digital wallet details and balance
public class WalletResponseDto {

    private Long walletId;
    private Long userId;
    private Double balance;
    private LocalDateTime createdAt;
    private String message;

    public WalletResponseDto() {
    }

    public WalletResponseDto(Long walletId, Long userId, Double balance, LocalDateTime createdAt, String message) {
        this.walletId = walletId;
        this.userId = userId;
        this.balance = balance;
        this.createdAt = createdAt;
        this.message = message;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
