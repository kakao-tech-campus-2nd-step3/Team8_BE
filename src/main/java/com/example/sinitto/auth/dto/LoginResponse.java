package com.example.sinitto.auth.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String redirectUrl,
        String email,
        boolean isSinitto,
        boolean isMember
) {
}
