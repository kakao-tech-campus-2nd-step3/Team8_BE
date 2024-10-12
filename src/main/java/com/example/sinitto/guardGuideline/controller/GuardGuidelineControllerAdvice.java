package com.example.sinitto.guardGuideline.controller;

import com.example.sinitto.guardGuideline.exception.GuardGuidelineNotFoundException;
import com.example.sinitto.guardGuideline.exception.SeniorAndGuardMemberMismatchException;
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

    @ExceptionHandler(SeniorAndGuardMemberMismatchException.class)
    public ResponseEntity<ProblemDetail> handleSeniorAndGuardMemberMismatchException(SeniorAndGuardMemberMismatchException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setType(URI.create("/error/member-mismatch"));
        problemDetail.setTitle("Senior and Guard Member Mismatch");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
