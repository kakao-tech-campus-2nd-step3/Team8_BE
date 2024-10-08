package com.example.sinitto.sinitto.dto;

public record SinittoRequest(
        String name,
        String phoneNumber,
        String email,
        String accountNumber,
        String bankName) {
}
