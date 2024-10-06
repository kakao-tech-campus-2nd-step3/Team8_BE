package com.example.sinitto.helloCall.entity;

import com.example.sinitto.helloCall.exception.InvalidStatusException;
import com.example.sinitto.helloCall.exception.TimeRuleException;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class HelloCall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private int price;
    @NotNull
    private int serviceTime;
    @NotNull
    private String content;
    @NotNull
    @Enumerated(EnumType.STRING)
    private HelloCall.Status status;

    @OneToOne
    @JoinColumn(name = "senior_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Senior senior;
    @OneToMany(mappedBy = "helloCall", cascade = CascadeType.REMOVE)
    private List<TimeSlot> timeSlots = new ArrayList<>();

    public HelloCall(LocalDate startDate, LocalDate endDate, int price, int serviceTime, String content, Senior senior) {
        if (startDate.isAfter(endDate)) {
            throw new TimeRuleException("시작날짜가 종료날짜 이후일 수 없습니다.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.content = content;
        this.status = Status.WAITING;
        this.price = price;
        this.serviceTime = serviceTime;
        this.senior = senior;
    }

    protected HelloCall() {

    }

    public Long getId() {
        return id;
    }

    public Senior getSenior() {
        return senior;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getContent() {
        return content;
    }

    public int getPrice() {
        return price;
    }

    public boolean checkUnAuthorization(Member member) {
        return !this.senior.getMember().equals(member);
    }

    public void checkStatusIsWaiting() {
        if (!this.status.equals(Status.WAITING)) {
            throw new InvalidStatusException("안부전화 서비스가 수행 대기중일 때만 삭제가 가능합니다.");
        }
    }

    public void updateHelloCall(LocalDate startDate, LocalDate endDate, int price, int serviceTime, String content) {
        if (!this.status.equals(Status.WAITING)) {
            throw new InvalidStatusException("안부전화 서비스가 수행 대기중일 때만 수정이 가능합니다.");
        }
        if (startDate.isAfter(endDate)) {
            throw new TimeRuleException("시작날짜가 종료날짜 이후일 수 없습니다.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.serviceTime = serviceTime;
        this.content = content;
    }

    public enum Status {
        WAITING,
        IN_PROGRESS,
        COMPLETE
    }
}
