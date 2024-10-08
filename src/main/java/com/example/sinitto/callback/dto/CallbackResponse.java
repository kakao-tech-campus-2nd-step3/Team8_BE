package com.example.sinitto.callback.dto;

import java.time.LocalDateTime;

public record CallbackResponse(
        Long callbackId,
        String seniorName,
        LocalDateTime postTime,
        String status,
        Long seniorId) {
}
