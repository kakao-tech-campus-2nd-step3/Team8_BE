package com.example.sinitto.dto;

import java.util.Date;

public record ReviewResponse(
        String name,
        int starCount,
        Date postDate,
        String content) {
}
