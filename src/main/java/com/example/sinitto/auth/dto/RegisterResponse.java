package com.example.sinitto.auth.dto;

public record RegisterResponse(
        String accessToken,
        String refreshToken,
        boolean isSinitto
) {
}
