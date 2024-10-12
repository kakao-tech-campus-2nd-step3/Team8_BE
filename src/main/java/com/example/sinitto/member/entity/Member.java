package com.example.sinitto.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String email;
    @NotNull
    private boolean isSinitto;

    public Member(String name, String phoneNumber, String email, boolean isSinitto) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isSinitto = isSinitto;
    }

    protected Member() {
    }

    public void updateMember(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getDepositMessage() {

        return name.substring(0, 3) + phoneNumber.substring(phoneNumber.length() - 4);
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

    public boolean isSinitto() {
        return isSinitto;
    }

    public String getEmail() {
        return email;
    }
}

