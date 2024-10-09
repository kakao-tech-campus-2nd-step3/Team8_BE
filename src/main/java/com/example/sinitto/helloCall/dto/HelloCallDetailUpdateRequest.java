package com.example.sinitto.helloCall.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record HelloCallDetailUpdateRequest(
        LocalDate startDate,
        LocalDate endDate,
        List<TimeSlot> timeSlots,
        int price,
        int serviceTime,
        String requirement
) {
    public record TimeSlot(
            String dayName,
            @JsonFormat(pattern = "kk:mm")
            LocalTime startTime,
            @JsonFormat(pattern = "kk:mm")
            LocalTime endTime) {
    }
}
