package com.example.sinitto.member.dto;

public record SignupRequest(
        String name,
        String phoneNumber,
        String email,
        boolean isSinitto
) {
}
