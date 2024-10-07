package com.example.sinitto.review.service;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.review.dto.ReviewRequest;
import com.example.sinitto.review.dto.ReviewResponse;
import com.example.sinitto.review.entity.Review;
import com.example.sinitto.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final MemberRepository memberRepository;

    public ReviewService(ReviewRepository reviewRepository, MemberRepository memberRepository) {
        this.reviewRepository = reviewRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public List<ReviewResponse> readAllReviews() {
        List<Review> reviewList = reviewRepository.findAll();

        return reviewList.stream().map(Review::mapToResponse).toList();
    }

    @Transactional
    public void createReview(Long memberId, ReviewRequest reviewRequest) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("이메일에 해당하는 멤버를 찾을 수 없습니다.")
        );
        Review review = new Review(reviewRequest.starCountForRequest(),
                reviewRequest.starCountForService(),
                reviewRequest.starCountForSatisfaction(),
                reviewRequest.content(),
                member);

        reviewRepository.save(review);
    }
}
