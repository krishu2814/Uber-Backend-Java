package com.krishu.uber.payment.exception;

// Custom runtime exception thrown when client input fails business rules or validation
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
