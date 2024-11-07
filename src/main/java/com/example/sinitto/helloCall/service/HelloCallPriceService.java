package com.example.sinitto.helloCall.service;

import com.example.sinitto.common.exception.BadRequestException;
import com.example.sinitto.helloCall.dto.HelloCallPriceRequest;
import com.example.sinitto.helloCall.dto.HelloCallPriceResponse;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

@Service
public class HelloCallPriceService {

    private static final int PRICE_PER_ONE_MINUTE = 50;
    private static final Map<String, DayOfWeek> DAY_NAME_MAP = Map.of(
            "월", DayOfWeek.MONDAY,
            "화", DayOfWeek.TUESDAY,
            "수", DayOfWeek.WEDNESDAY,
            "목", DayOfWeek.THURSDAY,
            "금", DayOfWeek.FRIDAY,
            "토", DayOfWeek.SATURDAY,
            "일", DayOfWeek.SUNDAY
    );

    public HelloCallPriceResponse calculateHelloCallPrice(HelloCallPriceRequest helloCallPriceRequest) {
        int totalServiceCount = calculateTotalServiceCount(helloCallPriceRequest);
        int serviceTime = helloCallPriceRequest.serviceTime();

        int price = totalServiceCount * serviceTime * PRICE_PER_ONE_MINUTE;

        return new HelloCallPriceResponse(price, totalServiceCount);
    }

    public int calculateTotalServiceCount(HelloCallPriceRequest helloCallPriceRequest) {
        int totalServiceCount = 0;

        LocalDate startDate = helloCallPriceRequest.startDate();
        LocalDate endDate = helloCallPriceRequest.endDate();

        for (HelloCallPriceRequest.TimeSlot timeSlot : helloCallPriceRequest.timeSlots()) {
            DayOfWeek targetDayOfWeek = convertDayStringToDayOfWeek(timeSlot.dayName());
            totalServiceCount += countOccurrencesOfDay(startDate, endDate, targetDayOfWeek);
        }

        return totalServiceCount;
    }

    private DayOfWeek convertDayStringToDayOfWeek(String dayName) {
        DayOfWeek dayOfWeek = DAY_NAME_MAP.get(dayName);
        if (dayOfWeek == null) {
            throw new BadRequestException("잘못된 dayName 입니다 : " + dayName);
        }
        return dayOfWeek;
    }

    private int countOccurrencesOfDay(LocalDate startDate, LocalDate endDate, DayOfWeek targetDayOfWeek) {
        LocalDate firstOccurrence = findFirstOccurrenceOfDay(startDate, targetDayOfWeek);

        int occurrences = 0;
        for (LocalDate date = firstOccurrence; !date.isAfter(endDate); date = date.plusWeeks(1)) {
            occurrences++;
        }

        return occurrences;
    }

    private LocalDate findFirstOccurrenceOfDay(LocalDate startDate, DayOfWeek targetDayOfWeek) {
        int daysToAdd = (targetDayOfWeek.getValue() - startDate.getDayOfWeek().getValue() + 7) % 7;
        return startDate.plusDays(daysToAdd);
    }
}
