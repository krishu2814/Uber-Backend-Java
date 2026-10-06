package com.krishu.uber.ride.enums;

// Payment settlement status for a trip
public enum PaymentStatus {
    PENDING,    // Ride completed, payment settlement pending
    COMPLETED,  // Payment successfully deducted from rider and credited to driver
    FAILED      // Payment settlement attempt failed (e.g., insufficient wallet funds)
}
