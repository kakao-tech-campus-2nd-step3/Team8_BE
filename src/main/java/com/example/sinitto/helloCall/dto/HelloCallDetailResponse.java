package com.example.sinitto.helloCall.dto;

import com.example.sinitto.helloCall.entity.TimeSlot;

import java.time.LocalDate;
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
}
