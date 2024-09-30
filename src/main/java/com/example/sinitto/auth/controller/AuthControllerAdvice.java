package com.example.sinitto.auth.controller;

import com.example.sinitto.auth.exception.JWTExpirationException;
import com.example.sinitto.auth.exception.KakaoRefreshTokenExpirationException;
import com.example.sinitto.auth.exception.TokenNotFoundException;
import com.example.sinitto.auth.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice(basePackages = "com.example.sinitto.auth")
public class AuthControllerAdvice {

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

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleTokenNotFoundException(TokenNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/token-not-found"));
        problemDetail.setTitle("Token Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(KakaoRefreshTokenExpirationException.class)
    public ResponseEntity<ProblemDetail> handleKakaoRefreshTokenExpirationException(KakaoRefreshTokenExpirationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                ex.getMessage());
        problemDetail.setType(URI.create("/errors/unauthorized-access-by-kakao-refresh-token-expiration"));
        problemDetail.setTitle("Unauthorized Access By Kakao Refresh Token Expiration");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }
}