package com.example.sinitto.auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class KakaoToken {

    @NotNull
    @CreatedDate
    protected LocalDateTime issuedAt;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @NotNull
    private String memberEmail;
    @NotNull
    private String accessToken;
    @NotNull
    private String refreshToken;
    @NotNull
    private int expiresIn;
    @NotNull
    private int refreshTokenExpiresIn;

    public KakaoToken(String memberEmail, String accessToken, String refreshToken,
                      int expiresIn, int refreshTokenExpiresIn) {
        this.memberEmail = memberEmail;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    protected KakaoToken() {
    }

    public boolean isAccessTokenExpired() {
        return LocalDateTime.now().isAfter(issuedAt.plusSeconds(expiresIn));
    }

    public boolean isRefreshTokenExpired() {
        return LocalDateTime.now().isAfter(issuedAt.plusSeconds(refreshTokenExpiresIn));
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getId() {
        return id;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void updateKakaoToken(String accessToken, String refreshToken,
                                 int expiresIn, int refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
        this.issuedAt = LocalDateTime.now();
    }
}
