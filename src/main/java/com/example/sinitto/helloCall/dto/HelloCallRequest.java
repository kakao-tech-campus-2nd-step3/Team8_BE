package com.example.sinitto.helloCall.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(example = """
        {
          "seniorId": 15,
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
          "price": 500,
          "serviceTime": 10,
          "requirement": "어머님께서 매일 아침 등산을 하시는 걸 좋아하세요. 요즘 날씨가 추워졌는데, 건강하게 등산을 잘 다니시는지 여쭤봐 주세요. 등산 이야기를 하면 기분이 좋아지실 거예요."
        }
        """)
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
            @JsonFormat(pattern = "HH:mm")
            LocalTime startTime,
            @JsonFormat(pattern = "HH:mm")
            LocalTime endTime) {
    }
}
