package com.example.sinitto.point.entity;

import com.example.sinitto.member.entity.Member;
import jakarta.persistence.*;

@Entity
public class Point {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int price;
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
