package com.example.sinitto.point.dto;

import com.example.sinitto.point.entity.PointLog;

import java.time.LocalDateTime;

public record PointLogWithDepositMessage(
        Long pointLogId,
        int price,
        LocalDateTime postTime,
        PointLog.Status status,
        String depositMessage
) {
}
