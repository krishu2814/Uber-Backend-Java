package com.krishu.uber.ride.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Request DTO submitted by a rider to request a new ride
public class RideRequestDto {

    @NotNull(message = "Rider ID cannot be null")
    private Long riderId;

    @NotBlank(message = "Pickup address cannot be empty")
    private String pickupAddress;

    @NotBlank(message = "Dropoff address cannot be empty")
    private String dropoffAddress;

    @NotNull(message = "Pickup latitude cannot be null")
    private Double pickupLatitude;

    @NotNull(message = "Pickup longitude cannot be null")
    private Double pickupLongitude;

    @NotNull(message = "Dropoff latitude cannot be null")
    private Double dropoffLatitude;

    @NotNull(message = "Dropoff longitude cannot be null")
    private Double dropoffLongitude;

    // Optional surge multiplier (defaults to 1.0)
    private Double surgeMultiplier;

    public RideRequestDto() {
    }

    public RideRequestDto(Long riderId, String pickupAddress, String dropoffAddress,
                          Double pickupLatitude, Double pickupLongitude,
                          Double dropoffLatitude, Double dropoffLongitude, Double surgeMultiplier) {
        this.riderId = riderId;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;
        this.pickupLatitude = pickupLatitude;
        this.pickupLongitude = pickupLongitude;
        this.dropoffLatitude = dropoffLatitude;
        this.dropoffLongitude = dropoffLongitude;
        this.surgeMultiplier = surgeMultiplier;
    }

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
    }

    public Double getDropoffLatitude() {
        return dropoffLatitude;
    }

    public void setDropoffLatitude(Double dropoffLatitude) {
        this.dropoffLatitude = dropoffLatitude;
    }

    public Double getDropoffLongitude() {
        return dropoffLongitude;
    }

    public void setDropoffLongitude(Double dropoffLongitude) {
        this.dropoffLongitude = dropoffLongitude;
    }

    public Double getSurgeMultiplier() {
        return surgeMultiplier;
    }

    public void setSurgeMultiplier(Double surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
    }
}
