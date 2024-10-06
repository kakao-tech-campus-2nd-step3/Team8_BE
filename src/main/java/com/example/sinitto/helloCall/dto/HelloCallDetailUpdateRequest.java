package com.example.sinitto.helloCall.dto;

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
            String day,
            LocalTime startTime,
            LocalTime endTime) {
    }
}
