package com.example.sinitto.auth.exception;

public class KakaoRefreshTokenExpirationException extends RuntimeException {
    public KakaoRefreshTokenExpirationException(String message) {
        super(message);
    }
}
