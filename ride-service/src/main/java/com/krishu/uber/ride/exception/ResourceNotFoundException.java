package com.krishu.uber.ride.exception;

// Thrown when a ride entity is not found in the database
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
