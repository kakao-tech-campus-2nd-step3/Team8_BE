package com.example.sinitto.dto;

public record GuardSignupRequest(
        String name,
        String email,
        String phoneNumber) {
}
