package com.example.sinitto.auth.dto;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
