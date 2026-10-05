package com.krishu.uber.driver.exception;

// Thrown when a driver entity or profile is not found in the database
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
