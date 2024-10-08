package com.example.sinitto.helloCall.controller;

import com.example.sinitto.helloCall.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice(basePackages = "com.example.sinitto.helloCall")
public class HelloCallControllerAdvice {

    @ExceptionHandler(TimeRuleException.class)
    public ResponseEntity<ProblemDetail> handleTimeRuleException(TimeRuleException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/time-rule-exception"));
        problemDetail.setTitle("Time Rule Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(HelloCallAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleHelloCallAlreadyExistsException(HelloCallAlreadyExistsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/hello-call-already-exist"));
        problemDetail.setTitle("Hello Call Already Exist");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(HelloCallNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleHelloCallNotFoundException(HelloCallNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/hello-call-not-found"));
        problemDetail.setTitle("Hello Call Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<ProblemDetail> handleInvalidStatusException(InvalidStatusException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/invalid-status-exception"));
        problemDetail.setTitle("Invalid Status Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(CompletionConditionNotFulfilledException.class)
    public ResponseEntity<ProblemDetail> handleCompletionConditionNotFulfilledException(CompletionConditionNotFulfilledException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/completion-condition-not-fulfilled"));
        problemDetail.setTitle("Completion Condition Not Fulfilled");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(TimeLogSequenceException.class)
    public ResponseEntity<ProblemDetail> handleTimeLogSequenceException(TimeLogSequenceException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/time-log-sequence-exception"));
        problemDetail.setTitle("Time Log Sequence Exception");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }
}
