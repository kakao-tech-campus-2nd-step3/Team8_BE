package com.example.sinitto.review.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.review.dto.ReviewResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    int starCountForRequest;

    @NotNull
    int starCountForService;

    @NotNull
    int starCountForSatisfaction;

    @CreatedDate
    LocalDate postDate;

    String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public Review(int starCountForRequest, int starCountForService, int starCountForSatisfaction, String content, Member member) {
        this.starCountForRequest = starCountForRequest;
        this.starCountForService = starCountForService;
        this.starCountForSatisfaction = starCountForSatisfaction;
        this.content = content;
        this.member = member;
    }

    public Review() {
    }

    public ReviewResponse mapToResponse() {
        double averageStarCount = (double) Math.round((double) ((starCountForRequest +
                starCountForSatisfaction +
                starCountForRequest) / 3) * 100) / 100;
        return new ReviewResponse(this.member.getName(), averageStarCount, this.postDate, this.content);
    }

    public Long getId() {
        return id;
    }

    public int getStarCountForRequest() {
        return starCountForRequest;
    }

    public int getStarCountForService() {
        return starCountForService;
    }

    public int getStarCountForSatisfaction() {
        return starCountForSatisfaction;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public String getContent() {
        return content;
    }

    public Member getMember() {
        return member;
    }
}
