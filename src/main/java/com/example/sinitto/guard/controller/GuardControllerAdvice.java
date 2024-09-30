package com.example.sinitto.guard.controller;

import com.example.sinitto.guard.exception.SeniorNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice(basePackages = "com.example.sinitto.guard")
public class GuardControllerAdvice {
    @ExceptionHandler(SeniorNotFoundException.class)
    public ProblemDetail handleSeniorNotFoundException(SeniorNotFoundException e){
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setType(URI.create("/error/senior-not-found"));
        problemDetail.setTitle("Senior Not Found");

        return problemDetail;
    }
}
