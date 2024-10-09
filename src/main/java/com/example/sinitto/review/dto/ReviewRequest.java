package com.example.sinitto.review.dto;

public record ReviewRequest(
        int starCountForRequest,
        int starCountForService,
        int starCountForSatisfaction,
        String content) {
}
