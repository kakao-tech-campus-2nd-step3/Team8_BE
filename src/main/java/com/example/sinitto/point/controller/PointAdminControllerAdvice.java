package com.example.sinitto.point.controller;

import com.example.sinitto.point.exception.InvalidPointLogStatusException;
import com.example.sinitto.point.exception.PointLogNotFoundException;
import com.example.sinitto.point.exception.PointNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice(basePackages = "com.example.sinitto.point")
public class PointAdminControllerAdvice {

    @ExceptionHandler(PointNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlePointNotFoundException(PointNotFoundException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setType(URI.create("/errors/not-found"));
        problemDetail.setTitle("Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(InvalidPointLogStatusException.class)
    public ResponseEntity<ProblemDetail> handleInvalidPointStatusException(InvalidPointLogStatusException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        problemDetail.setType(URI.create("/errors/conflict"));
        problemDetail.setTitle("Conflict");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(PointLogNotFoundException.class)
    public ResponseEntity<ProblemDetail> handlePointLogNotFoundException(PointLogNotFoundException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setType(URI.create("/errors/not-found"));
        problemDetail.setTitle("Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
}
