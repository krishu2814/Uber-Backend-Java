package com.krishu.uber.user.dto;

import com.krishu.uber.user.enums.Role;

// Response payload returned after registration or login
public class AuthResponse {
    private Long userId;
    private Long riderId; // Present if user is a rider
    private String name;
    private String email;
    private String phone;
    private Role role;
    private String message;

    public AuthResponse() {
    }

    public AuthResponse(Long userId, Long riderId, String name, String email, String phone, Role role, String message) {
        this.userId = userId;
        this.riderId = riderId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRiderId() {
        return riderId;
    }

    public void setRiderId(Long riderId) {
        this.riderId = riderId;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
