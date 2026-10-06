package com.krishu.uber.ride.exception;

// Thrown when an illegal state transition or invalid operation is attempted
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
