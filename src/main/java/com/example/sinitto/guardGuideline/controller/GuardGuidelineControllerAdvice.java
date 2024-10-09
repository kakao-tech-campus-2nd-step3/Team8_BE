package com.example.sinitto.guardGuideline.controller;

import com.example.sinitto.guardGuideline.exception.GuardGuidelineNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice(basePackages = "com.example.sinitto.guardGuideline")
public class GuardGuidelineControllerAdvice {
    @ExceptionHandler(GuardGuidelineNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleGuardGuidelineNotFoundException(GuardGuidelineNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setType(URI.create("/error/guideline-not-found"));
        problemDetail.setTitle("Guideline Not Found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
}
