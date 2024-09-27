package com.example.sinitto.member.dto;

public record SinittoSignupRequest(
        String name,
        String phoneNumber,
        String email,
        String accountNumber,
        String bankName) {
}
