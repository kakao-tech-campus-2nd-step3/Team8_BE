package com.example.sinitto.helloCall.entity;

import com.example.sinitto.auth.exception.UnauthorizedException;
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
    @JoinColumn(name = "member_id")
    private Member member;
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

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getMemberName() {
        return this.member.getName();
    }

    public void checkStatusIsWaiting() {
        if (status.canNotModifyOrDelete()) {
            throw new InvalidStatusException("안부전화 서비스가 수행 대기중일 때만 삭제가 가능합니다.");
        }
    }

    public void checkGuardIsCorrect(Member member) {
        if (!this.senior.getMember().equals(member)) {
            throw new UnauthorizedException("해당 시니어의 안부전화를 신청한 보호자가 아닙니다. 권한이 없습니다.");
        }
    }

    public boolean checkIsNotAfterEndDate() {
        return !(LocalDate.now().isAfter(this.endDate) || LocalDate.now().equals(this.endDate));
    }

    public boolean checkReportIsNotNull() {
        return this.report != null;
    }

    public void checkMemberIsRightSinitto(Member member) {
        if (this.member == null || !this.member.equals(member)) {
            throw new UnauthorizedException("해당 안부전화를 수행하는 시니또가 아닙니다.");
        }
    }

    public void changeStatusToInProgress() {
        if (status.canNotProgressStatus(Status.IN_PROGRESS)) {
            throw new InvalidStatusException("안부전화 서비스가 수행 대기중일 때만 진행중 상태로 나아갈 수 있습니다. 현재 상태 : " + this.status);
        }
        this.status = Status.IN_PROGRESS;
    }

    public void changeStatusToWaiting() {
        if (status.canNotRollBackStatus()) {
            throw new InvalidStatusException("안부전화 서비스가 수행중일 때만 진행중 상태로 돌아갈 수 있습니다. 현재 상태 : " + this.status);
        }
        this.status = Status.WAITING;
    }

    public void changeStatusToPendingComplete() {
        if (status.canNotProgressStatus(Status.PENDING_COMPLETE)) {
            throw new InvalidStatusException("안부전화 서비스가 수행중일 때만 완료 대기 상태로 나아갈 수 있습니다. 현재 상태 : " + this.status);
        }
        this.status = Status.PENDING_COMPLETE;
    }

    public void changeStatusToComplete() {
        if (status.canNotProgressStatus(Status.COMPLETE)) {
            throw new InvalidStatusException("안부전화 서비스가 완료 대기 일때만 완료 상태로 변경할 수 있습니다. 현재 상태 : " + this.status);
        }
        this.status = Status.COMPLETE;
    }

    public void updateHelloCall(LocalDate startDate, LocalDate endDate, int price, int serviceTime, String requirement) {
        if (status.canNotModifyOrDelete()) {
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
        COMPLETE;

        public boolean canNotProgressStatus(Status newStatus) {
            return !switch (this) {
                case WAITING -> newStatus.equals(IN_PROGRESS);
                case IN_PROGRESS -> newStatus.equals(PENDING_COMPLETE);
                case PENDING_COMPLETE -> newStatus.equals(COMPLETE);
                default -> false;
            };
        }

        public boolean canNotRollBackStatus() {
            return !this.equals(IN_PROGRESS);
        }

        public boolean canNotModifyOrDelete() {
            return !this.equals(WAITING);
        }
    }
}
