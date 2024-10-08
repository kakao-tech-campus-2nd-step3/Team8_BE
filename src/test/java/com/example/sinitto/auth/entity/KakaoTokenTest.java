package com.example.sinitto.auth.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class KakaoTokenTest {

    private KakaoToken kakaoToken;

    @BeforeEach
    void setup() {
        kakaoToken = new KakaoToken(
                "test@test.com",
                "access_token_123",
                "refresh_token_123",
                3600,
                86400
        );
    }

    @Test
    @DisplayName("KakaoToken 생성자 테스트")
    void constructorTest() {
        assertThat(kakaoToken.getMemberEmail()).isEqualTo("test@test.com");
        assertThat(kakaoToken.getAccessToken()).isEqualTo("access_token_123");
        assertThat(kakaoToken.getRefreshToken()).isEqualTo("refresh_token_123");
        assertThat(kakaoToken.getId()).isNull();
    }

    @Test
    @DisplayName("액세스 토큰 만료 여부 테스트")
    void isAccessTokenExpiredTest() {
        kakaoToken.updateKakaoToken(
                "access_token_123",
                "refresh_token_123",
                3600,
                86400
        );

        assertThat(kakaoToken.isAccessTokenExpired()).isFalse();

        kakaoToken.updateKakaoToken(
                "access_token_123",
                "refresh_token_123",
                3600,
                86400
        );
        kakaoToken.issuedAt = LocalDateTime.now().minusHours(2);

        assertThat(kakaoToken.isAccessTokenExpired()).isTrue();
    }

    @Test
    @DisplayName("리프레시 토큰 만료 여부 테스트")
    void isRefreshTokenExpiredTest() {
        kakaoToken.updateKakaoToken(
                "access_token_123",
                "refresh_token_123",
                3600,
                86400
        );

        assertThat(kakaoToken.isRefreshTokenExpired()).isFalse();

        kakaoToken.issuedAt = LocalDateTime.now().minusHours(25);

        assertThat(kakaoToken.isRefreshTokenExpired()).isTrue();
    }

    @Test
    @DisplayName("카카오 토큰 업데이트 테스트")
    void updateKakaoTokenTest() {
        kakaoToken.updateKakaoToken(
                "new_access_token_456",
                "new_refresh_token_456",
                7200,
                172800
        );

        assertThat(kakaoToken.getAccessToken()).isEqualTo("new_access_token_456");
        assertThat(kakaoToken.getRefreshToken()).isEqualTo("new_refresh_token_456");
        assertThat(kakaoToken.isAccessTokenExpired()).isFalse();
        assertThat(kakaoToken.isRefreshTokenExpired()).isFalse();
    }
}
