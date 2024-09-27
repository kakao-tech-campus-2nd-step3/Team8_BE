package com.example.sinitto.member.exception;

public class JWTExpirationException extends RuntimeException {
    public JWTExpirationException(String message) {
        super(message);
    }
}
