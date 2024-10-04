package com.example.sinitto.auth.exception;

public class JWTExpirationException extends RuntimeException {
    public JWTExpirationException(String message) {
        super(message);
    }
}
