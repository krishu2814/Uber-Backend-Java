package com.krishu.uber.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

// Request body submitted to top up a digital wallet
public class AddMoneyRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "1.0", message = "Amount must be at least ₹1.0")
    private Double amount;

    public AddMoneyRequest() {
    }

    public AddMoneyRequest(Long userId, Double amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
