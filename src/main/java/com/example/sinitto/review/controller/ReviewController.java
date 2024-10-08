package com.example.sinitto.review.controller;

import com.example.sinitto.common.annotation.MemberId;
import com.example.sinitto.review.dto.ReviewRequest;
import com.example.sinitto.review.dto.ReviewResponse;
import com.example.sinitto.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "리뷰", description = "서비스 리뷰 관련 API")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Operation(summary = "서비스 리뷰 리스트 보기", description = "서비스 리뷰를 리스트 형태로 보여줍니다.")
    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getReviewList() {
        return ResponseEntity.ok(reviewService.readAllReviews());
    }

    @Operation(summary = "서비스 리뷰 및 평가 제출", description = "시니또에 대한 별점과 작성한 평가 내용(선택사항)을 제출합니다.")
    @PostMapping
    public ResponseEntity<String> submitReview(@MemberId Long memberId, @RequestBody ReviewRequest reviewRequest) {
        reviewService.createReview(memberId, reviewRequest);
        return ResponseEntity.ok("리뷰가 성공적으로 제출되었습니다.");
    }
}
