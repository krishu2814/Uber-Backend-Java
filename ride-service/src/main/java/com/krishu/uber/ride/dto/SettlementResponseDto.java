package com.krishu.uber.ride.dto;

// Response body returned by payment-service after settling ride fare
public class SettlementResponseDto {

    private Long rideId;
    private Double fareAmount;
    private Long riderUserId;
    private Double riderRemainingBalance;
    private Long driverUserId;
    private Double driverNewBalance;
    private String status;
    private String message;

    public SettlementResponseDto() {
    }

    public SettlementResponseDto(Long rideId, Double fareAmount, Long riderUserId,
                                 Double riderRemainingBalance, Long driverUserId,
                                 Double driverNewBalance, String status, String message) {
        this.rideId = rideId;
        this.fareAmount = fareAmount;
        this.riderUserId = riderUserId;
        this.riderRemainingBalance = riderRemainingBalance;
        this.driverUserId = driverUserId;
        this.driverNewBalance = driverNewBalance;
        this.status = status;
        this.message = message;
    }

    public Long getRideId() {
        return rideId;
    }

    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }

    public Double getFareAmount() {
        return fareAmount;
    }

    public void setFareAmount(Double fareAmount) {
        this.fareAmount = fareAmount;
    }

    public Long getRiderUserId() {
        return riderUserId;
    }

    public void setRiderUserId(Long riderUserId) {
        this.riderUserId = riderUserId;
    }

    public Double getRiderRemainingBalance() {
        return riderRemainingBalance;
    }

    public void setRiderRemainingBalance(Double riderRemainingBalance) {
        this.riderRemainingBalance = riderRemainingBalance;
    }

    public Long getDriverUserId() {
        return driverUserId;
    }

    public void setDriverUserId(Long driverUserId) {
        this.driverUserId = driverUserId;
    }

    public Double getDriverNewBalance() {
        return driverNewBalance;
    }

    public void setDriverNewBalance(Double driverNewBalance) {
        this.driverNewBalance = driverNewBalance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
