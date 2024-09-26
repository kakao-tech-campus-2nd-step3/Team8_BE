package com.example.sinitto.point.entity;

import com.example.sinitto.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private int price;
    @OneToOne
    @JoinColumn(name = "member_id")
    @NotNull
    private Member member;

}
