package com.krishu.uber.driver.dto;

import com.krishu.uber.driver.enums.DriverStatus;

// Response DTO containing driver profile details
public class DriverResponseDto {

    private Long driverId;
    private Long userId;
    private String vehicleNumber;
    private String vehicleModel;
    private DriverStatus status;
    private Double latitude;
    private Double longitude;
    private Double rating;
    private String message;

    public DriverResponseDto() {
    }

    public DriverResponseDto(Long driverId, Long userId, String vehicleNumber, String vehicleModel,
                             DriverStatus status, Double latitude, Double longitude, Double rating, String message) {
        this.driverId = driverId;
        this.userId = userId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.message = message;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
