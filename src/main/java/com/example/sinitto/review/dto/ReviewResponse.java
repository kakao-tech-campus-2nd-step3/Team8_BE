package com.example.sinitto.review.dto;

import java.time.LocalDate;

public record ReviewResponse(
        String name,
        double averageStarCount,
        LocalDate postDate,
        String content) {
}
