package com.krishu.uber.driver.dto;

// DTO representing an available nearby driver returned to ride-service or rider app
public class NearbyDriverDto {

    private Long driverId;
    private Long userId;
    private String vehicleNumber;
    private String vehicleModel;
    private Double latitude;
    private Double longitude;
    private Double distanceKm;
    private Double rating;

    public NearbyDriverDto() {
    }

    public NearbyDriverDto(Long driverId, Long userId, String vehicleNumber, String vehicleModel,
                           Double latitude, Double longitude, Double distanceKm, Double rating) {
        this.driverId = driverId;
        this.userId = userId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceKm = distanceKm;
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

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
