package com.example.sinitto.sinitto.dto;

public record SinittoResponse(
        String name,
        String phoneNumber,
        String email,
        String accountNumber,
        String bankName) {
}
