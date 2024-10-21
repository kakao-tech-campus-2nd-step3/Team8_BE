package com.example.sinitto.helloCall.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(example = """
        {
          "startDate": "2024-10-01",
          "endDate": "2024-10-08",
          "timeSlots": [
            {
              "dayName": "수",
              "startTime": "18:00",
              "endTime": "20:00"
            },
            {
              "dayName": "금",
              "startTime": "18:00",
              "endTime": "20:00"
            }
          ],
          "serviceTime": 10
        }
        """)
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
