package com.example.sinitto.callback.dto;

import java.time.LocalDateTime;

public record CallbackResponse(
        String seniorName,
        LocalDateTime postTime,
        String status) {
}
