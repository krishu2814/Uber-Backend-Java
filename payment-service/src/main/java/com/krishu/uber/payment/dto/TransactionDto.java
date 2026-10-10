package com.krishu.uber.payment.dto;

import com.krishu.uber.payment.enums.TransactionType;
import java.time.LocalDateTime;

// DTO representing an entry in the transaction history
public class TransactionDto {

    private Long transactionId;
    private Long walletId;
    private Long rideId;
    private Double amount;
    private TransactionType type;
    private String description;
    private Double balanceAfter;
    private LocalDateTime timestamp;

    public TransactionDto() {
    }

    public TransactionDto(Long transactionId, Long walletId, Long rideId, Double amount,
                          TransactionType type, String description, Double balanceAfter, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.walletId = walletId;
        this.rideId = rideId;
        this.amount = amount;
        this.type = type;
        this.description = description;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(Double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
