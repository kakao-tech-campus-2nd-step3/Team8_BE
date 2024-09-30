package com.example.sinitto.member.controller;

import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.exception.NotUniqueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice(basePackages = "com.example.sinitto.member")
public class MemberControllerAdvice {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleMemberNotFoundException(MemberNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/member-not-found"));
        problemDetail.setTitle("Member Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(NotUniqueException.class)
    public ResponseEntity<ProblemDetail> handleNotUniqueException(NotUniqueException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.MULTI_STATUS,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/not-unique-state"));
        problemDetail.setTitle("Not Unique State");
        return ResponseEntity.status(HttpStatus.MULTI_STATUS).body(problemDetail);
    }

}
