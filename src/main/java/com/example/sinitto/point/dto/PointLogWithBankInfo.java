package com.example.sinitto.point.dto;

import com.example.sinitto.point.entity.PointLog;

import java.time.LocalDateTime;

public record PointLogWithBankInfo(
        Long pointLogId,
        int price,
        LocalDateTime postTime,
        PointLog.Status status,
        String bankName,
        String bankAccountNumber
) {
}
