package com.example.sinitto.member.controller;

import com.example.sinitto.member.exception.JWTExpirationException;
import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class MemberControllerAdvice {

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedException(UnauthorizedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/unauthorized-access"));
        problemDetail.setTitle("Unauthorized Access");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(JWTExpirationException.class)
    public ResponseEntity<ProblemDetail> handleJWTExpirationException(JWTExpirationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/unauthorized-access-by-jwt-expiration"));
        problemDetail.setTitle("Unauthorized Access By JWT Expiration");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleMemberNotFoundException(MemberNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/member-not-found"));
        problemDetail.setTitle("Member Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

}
