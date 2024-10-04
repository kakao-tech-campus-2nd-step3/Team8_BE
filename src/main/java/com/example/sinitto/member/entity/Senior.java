package com.example.sinitto.member.entity;

import com.example.sinitto.guard.dto.SeniorResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Senior {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String phoneNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public Senior(String name, String phoneNumber, Member member) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.member = member;
    }

    public void updateSenior(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public SeniorResponse mapToResponse() {
        return new SeniorResponse(this.id, this.name, this.phoneNumber);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Member getMember() {
        return member;
    }
}
