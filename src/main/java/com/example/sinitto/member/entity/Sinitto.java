package com.example.sinitto.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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
    private Member member;

}
