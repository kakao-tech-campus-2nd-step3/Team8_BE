package com.example.sinitto.sinitto.controller;

import com.example.sinitto.sinitto.exception.SinittoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;

@RestControllerAdvice(basePackages = "com.example.sinitto.sinitto")
public class SinittoControllerAdvice {
    @ExceptionHandler(SinittoNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleSinittoNotFoundException(SinittoNotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setType(URI.create("/error/sinitto-not-found"));
        problemDetail.setTitle("Sinitto Not Found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
}
