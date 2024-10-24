package com.example.sinitto.common.exception;

public class AccessTokenExpiredException extends RuntimeException {

    public AccessTokenExpiredException(String message) {
        super(message);
    }
}
