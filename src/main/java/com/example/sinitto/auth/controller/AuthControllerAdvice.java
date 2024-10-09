package com.example.sinitto.auth.controller;

import com.example.sinitto.auth.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice(basePackages = "com.example.sinitto.auth")
public class AuthControllerAdvice {

    private static final String UNAUTHORIZED_ACCESS_URI = "/errors/unauthorized-access";
    private static final String JWT_EXPIRATION_URI = "/errors/unauthorized-access-by-jwt-expiration";
    private static final String TOKEN_NOT_FOUND_URI = "/errors/token-not-found";
    private static final String KAKAO_REFRESH_TOKEN_EXPIRATION_URI = "/errors/unauthorized-access-by-kakao-refresh-token-expiration";
    private static final String KAKAO_EMAIL_NOT_FOUND_URI = "/errors/kakao-email-not-found";

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ProblemDetail> handleUnauthorizedException(UnauthorizedException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                ex.getMessage());
        problemDetail.setType(URI.create(UNAUTHORIZED_ACCESS_URI));
        problemDetail.setTitle("Unauthorized Access");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(JWTExpirationException.class)
    public ResponseEntity<ProblemDetail> handleJWTExpirationException(JWTExpirationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                ex.getMessage());
        problemDetail.setType(URI.create(JWT_EXPIRATION_URI));
        problemDetail.setTitle("Unauthorized Access By JWT Expiration");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleTokenNotFoundException(TokenNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                ex.getMessage());
        problemDetail.setType(URI.create(TOKEN_NOT_FOUND_URI));
        problemDetail.setTitle("Token Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(KakaoRefreshTokenExpirationException.class)
    public ResponseEntity<ProblemDetail> handleKakaoRefreshTokenExpirationException(KakaoRefreshTokenExpirationException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED,
                ex.getMessage());
        problemDetail.setType(URI.create(KAKAO_REFRESH_TOKEN_EXPIRATION_URI));
        problemDetail.setTitle("Unauthorized Access By Kakao Refresh Token Expiration");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(KakaoEmailNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleKakaoEmailNotFoundException(KakaoEmailNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND,
                ex.getMessage());
        problemDetail.setType(URI.create(KAKAO_EMAIL_NOT_FOUND_URI));
        problemDetail.setTitle("Kakao Email Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }
}
