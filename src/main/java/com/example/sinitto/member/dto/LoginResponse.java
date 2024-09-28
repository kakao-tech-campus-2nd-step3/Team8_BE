package com.example.sinitto.member.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String redirectUrl,
        String email
) {
}
