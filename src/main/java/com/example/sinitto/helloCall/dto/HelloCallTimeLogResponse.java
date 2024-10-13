package com.example.sinitto.helloCall.dto;

import java.time.LocalDateTime;

public record HelloCallTimeLogResponse(
        String sinittoName,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
