package com.example.sinitto.dto;

import java.time.LocalDateTime;

public record CallbackResponse(String seniorName,
                               LocalDateTime postTime,
                               String status) {
}
