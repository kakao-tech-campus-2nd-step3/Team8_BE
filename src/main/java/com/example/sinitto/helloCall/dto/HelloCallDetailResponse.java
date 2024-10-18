package com.example.sinitto.helloCall.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Schema(example = """
                {
                   "startDate": "2024-08-05",
                   "endDate": "2024-10-01",
                   "timeSlots": [
                     {
                       "dayName": "월",
                       "startTime": "18:00",
                       "endTime": "20:00"
                     },
                     {
                       "dayName": "수",
                       "startTime": "16:30",
                       "endTime": "18:30"
                     },
                     {
                       "dayName": "금",
                       "startTime": "18:00",
                       "endTime": "20:00"
                     }
                   ],
                   "requirement": "어머님께서 매일 아침 등산을 하시는 걸 좋아하세요. 요즘 날씨가 추워졌는데, 건강하게 등산을 잘 다니시는지 여쭤봐 주세요. 등산 이야기를 하면 기분이 좋아지실 거예요.",
                   "seniorName": "권지민",
                   "seniorPhoneNumber": "01013572468",
                   "price": 13000
                 }
        """)
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
