package com.example.sinitto.common.exception;

public class RefreshTokenStolenException extends RuntimeException {

    public RefreshTokenStolenException(String message) {
        super(message);
    }
}
