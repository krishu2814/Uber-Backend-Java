package com.krishu.uber.user.exception;

// Thrown when a user or rider is not found
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
