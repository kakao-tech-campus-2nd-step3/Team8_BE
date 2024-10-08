package com.example.sinitto.helloCall.entity;

import com.example.sinitto.auth.exception.UnauthorizedException;
import com.example.sinitto.helloCall.exception.InvalidStatusException;
import com.example.sinitto.helloCall.exception.TimeRuleException;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.entity.Sinitto;
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
    private String requirement;
    private String report;
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
    @ManyToOne
    @JoinColumn(name = "sinitto_id")
    private Sinitto sinitto;
    @OneToMany(mappedBy = "helloCall", cascade = CascadeType.REMOVE)
    private List<HelloCallTimeLog> helloCallTimeLogs = new ArrayList<>();

    public HelloCall(LocalDate startDate, LocalDate endDate, int price, int serviceTime, String requirement, Senior senior) {
        if (startDate.isAfter(endDate)) {
            throw new TimeRuleException("시작날짜가 종료날짜 이후일 수 없습니다.");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.requirement = requirement;
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

    public String getRequirement() {
        return requirement;
    }

    public int getPrice() {
        return price;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public Sinitto getSinitto() {
        return sinitto;
    }

    public void setSinitto(Sinitto sinitto) {
        this.sinitto = sinitto;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getSinittoName() {
        return this.sinitto.getMember().getName();
    }

    public List<HelloCallTimeLog> getHelloCallTimeLogs() {
        return helloCallTimeLogs;
    }

    public boolean checkUnAuthorization(Member member) {
        return !this.senior.getMember().equals(member);
    }

    public void checkStatusIsWaiting() {
        if (!this.status.equals(Status.WAITING)) {
            throw new InvalidStatusException("안부전화 서비스가 수행 대기중일 때만 삭제가 가능합니다.");
        }
    }

    public void checkSiniitoIsSame(Sinitto sinitto) {
        if (!this.sinitto.equals(sinitto)) {
            throw new UnauthorizedException("안부전화 서비스 리포트를 작성할 권한이 없습니다.");
        }
    }

    public void checkGuardIsCorrect(Member member) {
        if (!this.senior.getMember().equals(member)) {
            throw new UnauthorizedException("해당 시니어의 안부전화를 신청한 보호자가 아닙니다.");
        }
    }

    public boolean checkIsNotAfterEndDate() {
        return !(LocalDate.now().isAfter(this.endDate) || LocalDate.now().equals(this.endDate));
    }

    public boolean checkReportIsNotNull() {
        return this.report != null;
    }

    public void changeStatusToInProgress() {
        if (!this.status.equals(Status.WAITING)) {
            throw new InvalidStatusException("안부전화 서비스가 수행 대기중일 때만 진행중 상태로 변경할 수 있습니다. 현재 상태 : " + this.status);
        }
        this.status = Status.IN_PROGRESS;
    }

    public void changeStatusToWaiting() {
        if (!this.status.equals(Status.IN_PROGRESS)) {
            throw new InvalidStatusException("안부전화 서비스가 수행중일 때만 진행중 상태로 변경할 수 있습니다. 현재 상태 : " + this.status);
        }
        this.status = Status.WAITING;
    }

    public void changeStatusToPendingComplete() {
        if (!this.status.equals(Status.IN_PROGRESS)) {
            throw new InvalidStatusException("안부전화 서비스가 수행중일 때만 완료 대기 상태로 변경할 수 있습니다. 현재 상태 : " + this.status);
        }
        this.status = Status.PENDING_COMPLETE;
    }

    public void changeStatusToComplete() {
        if (!this.status.equals(Status.PENDING_COMPLETE)) {
            throw new InvalidStatusException("안부전화 서비스가 완료 대기 일때만 완료 상태로 변경할 수 있습니다. 현재 상태 : " + this.status);
        }
        this.status = Status.COMPLETE;
    }

    public void updateHelloCall(LocalDate startDate, LocalDate endDate, int price, int serviceTime, String requirement) {
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
        this.requirement = requirement;
    }

    public enum Status {
        WAITING,
        IN_PROGRESS,
        PENDING_COMPLETE,
        COMPLETE
    }
}
