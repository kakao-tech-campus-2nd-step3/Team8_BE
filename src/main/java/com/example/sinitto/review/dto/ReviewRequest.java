package com.example.sinitto.review.dto;

public record ReviewRequest(
        int starCount,
        String content) {
}
