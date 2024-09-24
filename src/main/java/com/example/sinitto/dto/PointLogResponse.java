package com.example.sinitto.dto;

import java.time.LocalDateTime;

public record PointLogResponse(
        LocalDateTime postTime,
        String requirementContent,
        int price,
        String type) {
}
