package com.krishu.uber.payment.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

// Request body submitted (e.g. by ride-service) to execute double-entry trip fare settlement
public class SettleRideRequest {

    @NotNull(message = "Ride ID cannot be null")
    private Long rideId;

    @NotNull(message = "Rider user ID cannot be null")
    private Long riderUserId;

    @NotNull(message = "Driver user ID cannot be null")
    private Long driverUserId;

    @NotNull(message = "Fare amount cannot be null")
    @DecimalMin(value = "0.01", message = "Fare amount must be greater than zero")
    private Double fareAmount;

    public SettleRideRequest() {
    }

    public SettleRideRequest(Long rideId, Long riderUserId, Long driverUserId, Double fareAmount) {
        this.rideId = rideId;
        this.riderUserId = riderUserId;
        this.driverUserId = driverUserId;
        this.fareAmount = fareAmount;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Long getRiderUserId() {
        return riderUserId;
    }

    public void setRiderUserId(Long riderUserId) {
        this.riderUserId = riderUserId;
    }

    public Long getDriverUserId() {
        return driverUserId;
    }

    public void setDriverUserId(Long driverUserId) {
        this.driverUserId = driverUserId;
    }

    public Double getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(Double fareAmount) {
        this.fareAmount = fareAmount;
    }
}
