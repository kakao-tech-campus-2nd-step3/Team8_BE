package com.example.sinitto.point.entity;

import com.example.sinitto.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public Point(int price, Member member) {
        this.price = price;
        this.member = member;
    }

    protected Point() {

    }

    public int getPrice() {
        return price;
    }

    public Member getMember() {
        return member;
    }

    public void earn(int earnedPrice) {
        this.price += earnedPrice;
    }

}
