package com.example.sinitto.helloCall.entity;

import com.example.sinitto.helloCall.exception.TimeRuleException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalTime;

@Entity
public class TimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String dayName;
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalTime endTime;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "hellocall_id")
    private HelloCall helloCall;

    public TimeSlot(String dayName, LocalTime startTime, LocalTime endTime, HelloCall helloCall) {
        if (startTime.isAfter(endTime)) {
            throw new TimeRuleException("시작시간이 종료시간 이후일 수 없습니다.");
        }
        this.dayName = dayName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.helloCall = helloCall;
    }

    protected TimeSlot() {

    }

    public String getDayName() {
        return dayName;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public HelloCall getHelloCall() {
        return helloCall;
    }

    public void updateTimeSlot(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new TimeRuleException("시작시간이 종료시간 이후일 수 없습니다.");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
