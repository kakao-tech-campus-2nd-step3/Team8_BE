package com.example.sinitto.review.dto;

import java.util.Date;

public record ReviewResponse(
        String name,
        int starCount,
        Date postDate,
        String content) {
}
