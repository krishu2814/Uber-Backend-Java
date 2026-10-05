package com.krishu.uber.driver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// Request body submitted to register a vehicle and driver profile
public class DriverRegistrationRequest {

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "Vehicle number cannot be empty")
    private String vehicleNumber;

    @NotBlank(message = "Vehicle model cannot be empty")
    private String vehicleModel;

    // Optional initial GPS latitude
    private Double latitude;

    // Optional initial GPS longitude
    private Double longitude;

    public DriverRegistrationRequest() {
    }

    public DriverRegistrationRequest(Long userId, String vehicleNumber, String vehicleModel, Double latitude, Double longitude) {
        this.userId = userId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
