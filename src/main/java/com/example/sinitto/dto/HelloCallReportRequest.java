package com.example.sinitto.dto;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public record HelloCallReportRequest(
        Date startDate,
        Date endDate,
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
