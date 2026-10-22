package com.krishu.uber.ride.dto;

// Request body submitted to payment-service to execute double-entry trip fare settlement
public class SettleRideRequest {

    private Long rideId;
    private Long riderUserId;
    private Long driverUserId;
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
