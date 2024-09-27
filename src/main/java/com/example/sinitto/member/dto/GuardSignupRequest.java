package com.example.sinitto.member.dto;

public record GuardSignupRequest(
        String name,
        String email,
        String phoneNumber) {
}
