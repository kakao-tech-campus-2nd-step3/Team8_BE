package com.example.sinitto.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Sinitto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String bankName;
    @NotNull
    private String accountNumber;
    @OneToOne
    @JoinColumn(name = "member_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public Sinitto() {
    }

    public Sinitto(String bankName, String accountNumber, Member member) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.member = member;
    }

    public void updateSinitto(String bankName, String accountNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

    public Long getId() {
        return id;
    }

    public String getBankName() {
        return bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Member getMember() {
        return member;
    }
}
