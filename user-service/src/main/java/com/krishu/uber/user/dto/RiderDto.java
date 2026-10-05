package com.krishu.uber.user.dto;

// Rider profile DTO
public class RiderDto {
    private Long riderId;
    private Long userId;
    private String name;
    private String email;
    private String phone;
    private Double rating;

    public RiderDto() {
    }

    public RiderDto(Long riderId, Long userId, String name, String email, String phone, Double rating) {
        this.riderId = riderId;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.rating = rating;
    }

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
