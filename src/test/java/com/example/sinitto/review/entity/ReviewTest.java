package com.example.sinitto.review.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.review.dto.ReviewResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ReviewTest {
    private Member member;
    private Review review;

    @BeforeEach
    public void setup() {
        member = new Member(
                "test",
                "01012345678",
                "test@test.com",
                true);
        review = new Review(5, 4, 3, "testContent", member);
    }

    @Test
    @DisplayName("Review 엔티티 생성자 테스트")
    void counstructorTest() {
        assertThat(review.getId()).isNull();
        assertThat(review.getStarCountForRequest()).isEqualTo(5);
        assertThat(review.getStarCountForService()).isEqualTo(4);
        assertThat(review.getStarCountForSatisfaction()).isEqualTo(3);
        assertThat(review.getPostDate()).isNull();
        assertThat(review.getContent()).isEqualTo("testContent");
        assertThat(review.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("mapToResponse 메소드 테스트")
    void mapToResponseTest() {
        ReviewResponse response = review.mapToResponse();
        assertThat(response.name()).isEqualTo(member.getName());
        assertThat(response.averageStarCount()).isEqualTo(4.0);
        assertThat(response.postDate()).isNull();
        assertThat(response.content()).isEqualTo(review.getContent());
    }
}
