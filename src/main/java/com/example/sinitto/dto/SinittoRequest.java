package com.example.sinitto.dto;

public record SinittoRequest(
        String name,
        String phoneNumber,
        String email,
        String accountNumber,
        String bankName) {
}
