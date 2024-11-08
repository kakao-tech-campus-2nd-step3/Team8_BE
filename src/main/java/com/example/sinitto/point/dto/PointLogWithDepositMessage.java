package com.example.sinitto.point.dto;

import com.example.sinitto.point.entity.PointLog;

public record PointLogWithDepositMessage(
        Long pointLogId,
        int price,
        String postTime,
        PointLog.Status status,
        String depositMessage
) {
}
