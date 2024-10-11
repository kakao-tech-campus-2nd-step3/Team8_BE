package com.example.sinitto.callback.entity;

import com.example.sinitto.callback.exception.AlreadyCompleteException;
import com.example.sinitto.callback.exception.AlreadyInProgressException;
import com.example.sinitto.callback.exception.AlreadyPendingCompleteException;
import com.example.sinitto.callback.exception.AlreadyWaitingException;
import com.example.sinitto.member.entity.Senior;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Callback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    private LocalDateTime postTime;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Callback.Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senior_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Senior senior;
    private Long assignedMemberId = null;

    public Callback(Status status, Senior senior) {
        this.status = status;
        this.senior = senior;
    }

    protected Callback() {

    }

    public void cancelAssignment() {

        if (this.status != Status.IN_PROGRESS) {
            throwStatusException("취소 요청은 진행 상태에서만 가능합니다.");
        }

        this.assignedMemberId = null;
    }

    public void changeStatusToWaiting() {

        if (this.status != Status.IN_PROGRESS) {
            throwStatusException("대기 상태로 변경은 진행 상태에서만 가능합니다.");
        }

        this.status = Status.WAITING;
    }

    public void assignMember(Long memberId) {

        if (this.status != Status.WAITING) {
            throwStatusException("멤버 할당은 대기 상태에서만 가능합니다.");
        }

        this.assignedMemberId = memberId;
    }

    public void changeStatusToInProgress() {

        if (this.status != Status.WAITING) {
            throwStatusException("진행 상태로 변경은 대기 상태에서만 가능합니다.");
        }

        this.status = Status.IN_PROGRESS;
    }

    public void changeStatusToPendingComplete() {

        if (this.status != Status.IN_PROGRESS) {
            throwStatusException("완료 대기 상태로 변경은 진행 상태에서만 가능합니다.");
        }

        this.status = Status.PENDING_COMPLETE;
    }

    public void changeStatusToComplete() {

        if (this.status != Status.PENDING_COMPLETE) {
            throwStatusException("완료 상태로 변경은 완료 대기 상태에서만 가능합니다.");
        }

        this.status = Status.COMPLETE;
    }

    private void throwStatusException(String message) {

        if (this.status == Status.PENDING_COMPLETE) {
            throw new AlreadyPendingCompleteException("완료 대기 상태의 콜백 입니다. " + message);
        }
        if (this.status == Status.COMPLETE) {
            throw new AlreadyCompleteException("완료 상태의 콜백 입니다. " + message);
        }
        if (this.status == Status.WAITING) {
            throw new AlreadyWaitingException("대기 상태의 콜백 입니다. " + message);
        }
        if (this.status == Status.IN_PROGRESS) {
            throw new AlreadyInProgressException("진행 상태의 콜백 입니다. " + message);
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public String getStatus() {
        return status.name();
    }

    public Senior getSenior() {
        return senior;
    }

    public String getSeniorName() {
        return senior.getName();
    }

    public Long getSeniorId() {
        return senior.getId();
    }

    public Long getAssignedMemberId() {
        return assignedMemberId;
    }

    public enum Status {
        WAITING,
        IN_PROGRESS,
        PENDING_COMPLETE,
        COMPLETE
    }

}
