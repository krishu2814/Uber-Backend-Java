package com.krishu.uber.driver.entity;

import com.krishu.uber.driver.enums.DriverStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

// JPA Entity representing a Driver registered in the Uber platform
@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // References the user ID in user-service
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    // Vehicle license plate number (e.g., DL-01-AB-1234)
    @Column(name = "vehicle_number", nullable = false, unique = true)
    private String vehicleNumber;

    // Vehicle make and model (e.g., Honda City, Maruti Swift)
    @Column(name = "vehicle_model", nullable = false)
    private String vehicleModel;

    // Current driver availability status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status = DriverStatus.AVAILABLE;

    // Live GPS coordinates
    @Column(name = "current_latitude")
    private Double currentLatitude;

    @Column(name = "current_longitude")
    private Double currentLongitude;

    // Driver rating (out of 5.0)
    @Column(nullable = false)
    private Double rating = 5.0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Default constructor required by JPA
    public Driver() {
    }

    // Parameterized constructor
    public Driver(Long userId, String vehicleNumber, String vehicleModel, Double currentLatitude, Double currentLongitude) {
        this.userId = userId;
        this.vehicleNumber = vehicleNumber;
        this.vehicleModel = vehicleModel;
        this.currentLatitude = currentLatitude;
        this.currentLongitude = currentLongitude;
        this.status = DriverStatus.AVAILABLE;
        this.rating = 5.0;
    }

    // Automatically set timestamps on creation
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = DriverStatus.AVAILABLE;
        }
        if (this.rating == null) {
            this.rating = 5.0;
        }
    }

    // Automatically update timestamp on modification
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
