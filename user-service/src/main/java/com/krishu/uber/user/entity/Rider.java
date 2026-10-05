package com.krishu.uber.user.entity;

import jakarta.persistence.*;

// Specific customer profile for riders who book rides
@Entity
@Table(name = "riders")
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link this rider to user account
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Customer rating (default 5.0 for newly registered riders)
    private Double rating = 5.0;

    // Default constructor
    public Rider() {
    }

    public Rider(User user, Double rating) {
        this.user = user;
        this.rating = rating != null ? rating : 5.0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
