package com.example.sinitto.helloCall.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record HelloCallRequest(
        Long seniorId,
        LocalDate startDate,
        LocalDate endDate,
        List<TimeSlot> timeSlots,
        int price,
        int serviceTime,
        String requirement
) {
    public record TimeSlot(
            String dayName,
            LocalTime startTime,
            LocalTime endTime) {
    }
}
