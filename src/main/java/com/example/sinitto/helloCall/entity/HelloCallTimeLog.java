package com.example.sinitto.helloCall.entity;

import com.example.sinitto.member.entity.Member;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
public class HelloCallTimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startDateAndTime;
    private LocalDateTime endDateAndTime;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "helloCall_id")
    private HelloCall helloCall;
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sinitto_id")
    private Member member;

    public HelloCallTimeLog(HelloCall helloCall, Member member) {
        this.helloCall = helloCall;
        this.member = member;
    }

    public HelloCallTimeLog(HelloCall helloCall, Member member, LocalDateTime startDateAndTime, LocalDateTime endDateAndTime) {
        this.helloCall = helloCall;
        this.startDateAndTime = startDateAndTime;
        this.endDateAndTime = endDateAndTime;
        this.member = member;
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
        return this.member.getName();
    }

    public HelloCall getHelloCall() {
        return helloCall;
    }

    public Member getSinitto() {
        return member;
    }
}
