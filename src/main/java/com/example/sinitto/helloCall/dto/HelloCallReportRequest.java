package com.example.sinitto.helloCall.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record HelloCallReportRequest(
        LocalDate startDate,
        LocalDate endDate,
        List<TimeSlot> timeSlots,
        String content
) {

    public record TimeSlot(
            String day,
            LocalTime startTime,
            LocalTime endTime,
            int serviceTime) {
    }
}
