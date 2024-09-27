package com.example.sinitto.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class KakaoToken {

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
    private int expires_in;
    @NotNull
    private int refresh_token_expires_in;
    @NotNull
    @CreatedDate
    private LocalDateTime issuedAt;

    public KakaoToken(String memberEmail, String accessToken, String refreshToken,
                      int expires_in, int refresh_token_expires_in) {
        this.memberEmail = memberEmail;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expires_in = expires_in;
        this.refresh_token_expires_in = refresh_token_expires_in;
    }

    public KakaoToken() {
    }

    public boolean isAccessTokenExpired() {
        return LocalDateTime.now().isAfter(issuedAt.plusSeconds(expires_in));
    }

    public boolean isRefreshTokenExpired() {
        return LocalDateTime.now().isAfter(issuedAt.plusSeconds(refresh_token_expires_in));
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void updateKakaoToken(String accessToken, String refreshToken,
                                 int expires_in, int refresh_token_expires_in) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expires_in = expires_in;
        this.refresh_token_expires_in = refresh_token_expires_in;
        this.issuedAt = LocalDateTime.now();
    }
}
