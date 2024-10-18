package com.example.sinitto.point.dto;

import com.example.sinitto.point.entity.PointLog;

import java.time.LocalDateTime;

public record PointLogResponse(
        LocalDateTime postTime,
        String content,
        int price,
        PointLog.Status status
) {
}
