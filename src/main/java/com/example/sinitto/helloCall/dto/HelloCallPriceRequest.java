package com.example.sinitto.helloCall.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record HelloCallPriceRequest(
        LocalDate startDate,
        LocalDate endDate,
        List<TimeSlot> timeSlots,
        int serviceTime
) {
    public record TimeSlot(
            String dayName,
            @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,
            @JsonFormat(pattern = "HH:mm")
            LocalTime endTime) {
    }
}
