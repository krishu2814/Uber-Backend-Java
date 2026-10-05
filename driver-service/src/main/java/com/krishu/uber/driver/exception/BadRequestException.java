package com.krishu.uber.driver.exception;

// Thrown when invalid parameters or conflicting operations are submitted
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
