package com.example.sinitto.callback.dto;

import java.time.LocalDateTime;

public record CallbackForSinittoResponse(
        Long callbackId,
        String seniorName,
        LocalDateTime postTime,
        String status,
        Long seniorId,
        boolean isAssignedToSelf,
        String seniorPhoneNumber
) {
}