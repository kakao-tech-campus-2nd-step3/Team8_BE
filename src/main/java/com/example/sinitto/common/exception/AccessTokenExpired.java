package com.example.sinitto.common.exception;

public class AccessTokenExpired extends RuntimeException {

    public AccessTokenExpired(String message) {
        super(message);
    }
}
