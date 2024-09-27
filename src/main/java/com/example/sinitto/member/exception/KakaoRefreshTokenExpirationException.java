package com.example.sinitto.member.exception;

public class KakaoRefreshTokenExpirationException extends RuntimeException {
    public KakaoRefreshTokenExpirationException(String message) {
        super(message);
    }
}
