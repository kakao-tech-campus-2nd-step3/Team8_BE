package com.example.sinitto.helloCall.dto;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

public record HelloCallDetailResponse(
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
