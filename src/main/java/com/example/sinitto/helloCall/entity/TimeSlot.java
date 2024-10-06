package com.example.sinitto.helloCall.entity;

import com.example.sinitto.helloCall.exception.TimeRuleException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@Entity
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String day;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @ManyToOne
    @JoinColumn(name = "hellocall_id")
    private HelloCall helloCall;

    public TimeSlot(String day, LocalTime startTime, LocalTime endTime, HelloCall helloCall) {
        if (startTime.isAfter(endTime)) {
            throw new TimeRuleException("시작시간이 종료시간 이후일 수 없습니다.");
        }
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.helloCall = helloCall;
    }

    protected TimeSlot() {

    }

    public String getDay() {
        return day;
    }

    public void updateTimeSlot(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new TimeRuleException("시작시간이 종료시간 이후일 수 없습니다.");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
