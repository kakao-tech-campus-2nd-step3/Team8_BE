package com.example.sinitto.review.service;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.review.dto.ReviewRequest;
import com.example.sinitto.review.dto.ReviewResponse;
import com.example.sinitto.review.entity.Review;
import com.example.sinitto.review.repository.ReviewRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@MockitoSettings
public class ReviewServiceTest {
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    ReviewService reviewService;

    @Test
    @DisplayName("readAllReviews 메소드 테스트")
    public void readAllReviewsTest(){
        //given
        Member member = new Member("testName", "01000000000", "test@test.com", true);
        List<Review> reviewList = new ArrayList<>();
        reviewList.add(new Review(5, "testContent", member));
        when(reviewRepository.findAll()).thenReturn(reviewList);
        //when
        List<ReviewResponse> reviewResponseList = reviewService.readAllReviews();
        //then
        assertEquals(1, reviewResponseList.size());
        assertEquals("testName", reviewResponseList.getFirst().name());
        assertEquals("testContent", reviewResponseList.getFirst().content());
        assertEquals(5, reviewResponseList.getFirst().starCount());
    }

    @Test
    @DisplayName("createReview 메소드 테스트")
    public void createReviewTest(){
        //given
        Member member = new Member("testName", "01000000000", "test@test.com", true);
        Long memberId = 1L;
        int starCount = 5;
        String content = "testContent";
        ReviewRequest reviewRequest = new ReviewRequest(starCount, content);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        //when
        reviewService.createReview(memberId, reviewRequest);

        //then
        verify(reviewRepository, times(1)).save(any());
    }
}
