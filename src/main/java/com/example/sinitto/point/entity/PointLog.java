package com.example.sinitto.point.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.point.exception.InvalidPointLogStatusException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class PointLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String content;
    @NotNull
    private int price;
    @CreatedDate
    private LocalDateTime postTime;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PointLog.Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;


    public PointLog(String content, Member member, int price, Status status) {
        this.content = content;
        this.member = member;
        this.price = price;
        this.status = status;
    }

    protected PointLog() {

    }

    public void changeStatusToChargeWaiting() {

        checkStatusChange(Status.CHARGE_REQUEST);

        this.status = Status.CHARGE_WAITING;
    }

    public void changeStatusToChargeComplete() {

        checkStatusChange(Status.CHARGE_WAITING);

        this.status = Status.CHARGE_COMPLETE;
    }

    public void changeStatusToChargeFail() {

        checkStatusChange(Status.CHARGE_WAITING);

        this.status = Status.CHARGE_FAIL;
    }

    public void changeStatusToWithdrawWaiting() {

        checkStatusChange(Status.WITHDRAW_REQUEST);

        this.status = Status.WITHDRAW_WAITING;
    }

    public void changeStatusToWithdrawComplete() {

        checkStatusChange(Status.WITHDRAW_WAITING);

        this.status = Status.WITHDRAW_COMPLETE;
    }

    public void changeStatusToWithdrawFail() {

        checkStatusChange(Status.WITHDRAW_WAITING);

        this.status = Status.WITHDRAW_FAIL_AND_RESTORE_POINT;
    }

    private void checkStatusChange(Status wantStatus) {

        if (this.status != wantStatus) {
            throw new InvalidPointLogStatusException(String.format("현재 %s 상태입니다. 이 상태에서는 %s 로의 전환이 불가합니다.", this.status, wantStatus));
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public int getPrice() {
        return price;
    }

    public Status getStatus() {
        return status;
    }

    public String getContent() {
        return content;
    }

    public enum Status {
        EARN,
        SPEND_COMPLETE,
        SPEND_CANCEL,
        WITHDRAW_REQUEST,
        WITHDRAW_WAITING,
        WITHDRAW_COMPLETE,
        WITHDRAW_FAIL_AND_RESTORE_POINT,
        CHARGE_REQUEST,
        CHARGE_WAITING,
        CHARGE_COMPLETE,
        CHARGE_FAIL
    }

    public enum Content {
        COMPLETE_CALLBACK_AND_EARN("콜백 완료로 인한 포인트 적립"),
        COMPLETE_HELLO_CALL_AND_EARN("안부 전화 완료로 인한 포인트 적립"),
        SPEND_COMPLETE_CALLBACK("콜백 신청으로 인한 포인트 차감"),
        SPEND_COMPLETE_HELLO_CALL("안부전화 신청으로 인한 포인트 차감"),
        SPEND_CANCEL_HELLO_CALL("안부전화 신청 취소로 인한 포인트 환불"),
        CHARGE_REQUEST("포인트 충전"),
        WITHDRAW_REQUEST("포인트 출금");

        private final String message;

        Content(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
