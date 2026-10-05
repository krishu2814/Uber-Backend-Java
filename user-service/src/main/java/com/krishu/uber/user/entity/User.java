package com.krishu.uber.user.entity;

import com.krishu.uber.user.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;

// Base user entity storing authentication details for riders and drivers
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Full name of the user
    @Column(nullable = false)
    private String name;

    // Unique email address used as username to log in
    @Column(nullable = false, unique = true)
    private String email;

    // Password for account
    @Column(nullable = false)
    private String password;

    // Contact mobile number
    @Column(nullable = false, unique = true)
    private String phone;

    // Role (RIDER, DRIVER, or ADMIN)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt;

    // Default constructor required by JPA
    public User() {
    }

    // Parameterized constructor
    public User(String name, String email, String password, String phone, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
    }

    // Automatically set timestamp when user registers
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
