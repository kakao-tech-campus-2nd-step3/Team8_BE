package com.example.sinitto.helloCall.service;

import com.example.sinitto.helloCall.dto.HelloCallPriceRequest;
import com.example.sinitto.helloCall.dto.HelloCallPriceResponse;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
public class HelloCallPriceService {

    private static final int PRICE_PER_ONE_MINUTE = 50;

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
            DayOfWeek targetDayOfWeek = convertDayStringToDayOfWeek(timeSlot.day());
            totalServiceCount += countOccurrencesOfDay(startDate, endDate, targetDayOfWeek);
        }

        return totalServiceCount;
    }

    private DayOfWeek convertDayStringToDayOfWeek(String day) {
        return switch (day) {
            case "월" -> DayOfWeek.MONDAY;
            case "화" -> DayOfWeek.TUESDAY;
            case "수" -> DayOfWeek.WEDNESDAY;
            case "목" -> DayOfWeek.THURSDAY;
            case "금" -> DayOfWeek.FRIDAY;
            case "토" -> DayOfWeek.SATURDAY;
            case "일" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("잘못된 day 입니다 : " + day);
        };
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
