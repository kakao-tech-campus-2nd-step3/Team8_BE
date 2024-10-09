package com.example.sinitto.helloCall.dto;

import java.time.LocalDate;

public record HelloCallReportResponse(
        LocalDate startDate,
        LocalDate endDate,
        String sinittoName,
        String report
) {
}
