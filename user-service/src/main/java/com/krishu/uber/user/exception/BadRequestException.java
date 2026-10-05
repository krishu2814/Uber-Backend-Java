package com.krishu.uber.user.exception;

// Thrown when invalid data or business rule is violated
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
