package com.krishu.uber.ride.enums;

// Lifecycle states of a ride from booking request to completion or cancellation
public enum RideStatus {
    REQUESTED,  // Rider created booking request, waiting for a driver to accept
    ACCEPTED,   // Driver accepted the ride request and is heading to pickup point
    ARRIVED,    // Driver has reached the pickup location
    STARTED,    // Driver verified the 4-digit OTP and the trip is underway
    COMPLETED,  // Trip has safely reached the destination and concluded
    CANCELLED   // Ride was cancelled prior to trip commencement
}
