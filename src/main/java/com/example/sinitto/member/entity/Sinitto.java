package com.example.sinitto.member.entity;

import jakarta.persistence.*;

@Entity
public class Sinitto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bankName;
    private String accountNumber;
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
