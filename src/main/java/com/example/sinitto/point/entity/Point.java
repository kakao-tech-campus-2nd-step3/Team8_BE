package com.example.sinitto.point.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.point.exception.NotEnoughPointException;
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

    public void earn(int priceToAdd) {

        this.price += priceToAdd;
    }

    public void deduct(int priceToDeduct) {

        if (this.price < priceToDeduct) {
            throw new NotEnoughPointException(String.format("보유한 포인트(%d) 보다 더 많은 포인트에 대한 출금요청입니다", this.price));
        }

        this.price -= priceToDeduct;
    }

    public boolean isSufficientForDeduction(int priceToDeduct) {

        return this.price >= priceToDeduct;
    }
}
