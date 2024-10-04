package com.example.sinitto.review.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.review.dto.ReviewResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    int starCount;

    @CreatedDate
    Date postDate;

    @NotNull
    String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public Review(int starCount, String content, Member member){
        this.starCount = starCount;
        this.content = content;
        this.member = member;
    }

    public Review(){}

    public ReviewResponse mapToResponse(){
        return new ReviewResponse(this.member.getName(), this.starCount, this.postDate, this.content);
    }

    public Long getId() {
        return id;
    }

    public int getStarCount() {
        return starCount;
    }

    public Date getPostDate() {
        return postDate;
    }

    public String getContent() {
        return content;
    }

    public Member getMember(){
        return member;
    }
}
