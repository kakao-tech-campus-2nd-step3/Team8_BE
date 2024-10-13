package com.example.sinitto.auth.exception;

public class KakaoEmailNotFoundException extends RuntimeException {
    public KakaoEmailNotFoundException(String message) {
        super(message);
    }
}
