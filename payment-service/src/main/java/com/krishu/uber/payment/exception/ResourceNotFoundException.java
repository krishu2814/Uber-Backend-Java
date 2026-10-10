package com.krishu.uber.payment.exception;

// Thrown when a wallet or transaction is not found
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
