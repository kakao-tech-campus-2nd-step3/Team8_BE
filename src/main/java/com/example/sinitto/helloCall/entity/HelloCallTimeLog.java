package com.example.sinitto.helloCall.entity;

import com.example.sinitto.sinitto.entity.SinittoBankInfo;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class HelloCallTimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDateAndTime;
    private LocalDateTime endDateAndTime;
    @ManyToOne
    @JoinColumn(name = "helloCall_id")
    private HelloCall helloCall;
    @ManyToOne
    @JoinColumn(name = "sinitto_id")
    private SinittoBankInfo sinittoBankInfo;

    public HelloCallTimeLog(HelloCall helloCall, SinittoBankInfo sinittoBankInfo) {
        this.helloCall = helloCall;
        this.sinittoBankInfo = sinittoBankInfo;
    }

    public HelloCallTimeLog(HelloCall helloCall, SinittoBankInfo sinittoBankInfo, LocalDateTime startDateAndTime, LocalDateTime endDateAndTime) {
        this.helloCall = helloCall;
        this.startDateAndTime = startDateAndTime;
        this.endDateAndTime = endDateAndTime;
        this.sinittoBankInfo = sinittoBankInfo;
    }

    protected HelloCallTimeLog() {
    }

    public LocalDateTime getStartDateAndTime() {
        return startDateAndTime;
    }

    public void setStartDateAndTime(LocalDateTime startDateAndTime) {
        this.startDateAndTime = startDateAndTime;
    }

    public LocalDateTime getEndDateAndTime() {
        return endDateAndTime;
    }

    public void setEndDateAndTime(LocalDateTime endDateAndTime) {
        this.endDateAndTime = endDateAndTime;
    }

    public String getSinittoName() {
        return this.sinittoBankInfo.getMember().getName();
    }

    public HelloCall getHelloCall() {
        return helloCall;
    }

    public SinittoBankInfo getSinitto() {
        return sinittoBankInfo;
    }
}
