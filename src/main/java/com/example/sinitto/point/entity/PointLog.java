package com.example.sinitto.point.entity;

import com.example.sinitto.member.entity.Member;
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
    private int price;
    @CreatedDate
    private LocalDateTime postTime;
    @NotNull
    @Enumerated(EnumType.STRING)
    private PointLog.Status status;
    @NotNull
    String content;
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
        SPEND_RESERVE,
        SPEND_COMPLETE,
        SPEND_CANCEL,
        REFUND,
        WITHDRAW_REQUEST,
        WITHDRAW_WAITING,
        WITHDRAW_COMPLETE,
        CHARGE_REQUEST,
        CHARGE_WAITING,
        CHARGE_COMPLETE
    }

}
