package com.example.sinitto.point.dto;

import java.time.LocalDateTime;

public record PointLogResponse(
        LocalDateTime postTime,
        String requirementContent,
        int price,
        String type) {
}
