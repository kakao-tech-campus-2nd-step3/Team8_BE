package com.example.sinitto.dto;

public record ReviewRequest(
        int starCount,
        String content) {
}
