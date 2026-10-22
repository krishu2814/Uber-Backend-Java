package com.krishu.uber.ride.dto;

// DTO representing driver profile data received from driver-service
public class DriverDto {

    private Long driverId;
    private Long userId;
    private String vehicleNumber;
    private String vehicleModel;
    private String status;
    private Double currentLatitude;
    private Double currentLongitude;
    private Double rating;

    public DriverDto() {
    }

    public DriverDto(Long driverId, Long userId, String vehicleNumber, String vehicleModel,
                     String status, Double currentLatitude, Double currentLongitude, Double rating) {
        this.driverId = driverId;
        this.userId = userId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.status = status;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.rating = rating;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(Double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public Double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(Double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
