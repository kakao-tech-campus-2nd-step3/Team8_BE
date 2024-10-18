package com.example.sinitto.callback.dto;

import java.time.LocalDateTime;

public record CallbackUsageHistoryResponse(
        Long callbackId,
        String seniorName,
        LocalDateTime postTime,
        String status
) {
}
