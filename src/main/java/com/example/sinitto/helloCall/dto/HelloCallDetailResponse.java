package com.example.sinitto.helloCall.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record HelloCallDetailResponse(
        LocalDate startDate,
        LocalDate endDate,
        List<TimeSlot> timeSlots,
        String requirement,
        String seniorName,
        String seniorPhoneNumber,
        int price
) {
    public record TimeSlot(
            String dayName,
            @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,
            @JsonFormat(pattern = "HH:mm")
            LocalTime endTime) {
    }
}
