package com.example.sinitto.dto;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelloCallReportRequest {

    Date startDate;
    Date endDate;
    List<TimeSlot> timeSlots = new ArrayList<>();
    String content;

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public String getContent() {
        return content;
    }

    static class TimeSlot {
        String day;
        LocalTime startTime;
        LocalTime endTime;
        int serviceTime;
    }
}
