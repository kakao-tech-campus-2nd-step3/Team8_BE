package com.example.sinitto.member.dto;

public record RegisterResponse(
        String accessToken,
        String refreshToken,
        boolean isSinitto
) {
}
