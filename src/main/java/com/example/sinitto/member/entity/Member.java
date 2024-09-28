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

    public Member() {
    }

    public Long getId() {
        return id;
    }
}
