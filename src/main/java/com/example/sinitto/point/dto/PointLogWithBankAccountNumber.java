package com.example.sinitto.point.dto;

import com.example.sinitto.point.entity.PointLog;

import java.time.LocalDateTime;

public record PointLogWithBankAccountNumber(
        Long pointLogId,
        Integer price,
        LocalDateTime postTime,
        PointLog.Status type,
        String bankAccountNumber
) {
}
