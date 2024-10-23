package com.example.sinitto.common.exception;

public class RefreshTokenStolen extends RuntimeException {

    public RefreshTokenStolen(String message) {
        super(message);
    }
}
