package com.example.sinitto.auth.service;

import com.example.sinitto.auth.dto.KakaoTokenResponse;
import com.example.sinitto.auth.entity.KakaoToken;
import com.example.sinitto.auth.repository.KakaoTokenRepository;
import com.example.sinitto.common.exception.InvalidJwtException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MockitoSettings
public class KakaoTokenServiceTest {
    @Mock
    private KakaoTokenRepository kakaoTokenRepository;

    @InjectMocks
    private KakaoTokenService kakaoTokenService;

    @Test
    @DisplayName("saveKakaoToken 메소드 테스트")
    void saveKakaoTokenTestInRepository() {
        //given
        String email = "test@email.com";
        KakaoTokenResponse kakaoTokenResponse = mock(KakaoTokenResponse.class);
        KakaoToken kakaoToken = mock(KakaoToken.class);

        when(kakaoTokenRepository.findByMemberEmail(email)).thenReturn(Optional.of(kakaoToken));

        //when
        kakaoTokenService.saveKakaoToken(email, kakaoTokenResponse);

        //then
        verify(kakaoToken, times(1)).updateKakaoToken(kakaoTokenResponse.accessToken(), kakaoTokenResponse.refreshToken(),
                kakaoTokenResponse.expiresIn(), kakaoTokenResponse.refreshTokenExpiresIn());
        verify(kakaoTokenRepository, times(1)).save(any(KakaoToken.class));
    }

    @Test
    @DisplayName("getValidAccessTokenInServer 메소드 테스트 - accessToken 만료 전")
    void getValidAccessTokenInServerTestWhenAccessTokenIsNotExpired() {
        //given
        String email = "test@email.com";
        KakaoToken kakaoToken = mock(KakaoToken.class);

        when(kakaoTokenRepository.findByMemberEmail(email)).thenReturn(Optional.of(kakaoToken));
        when(kakaoToken.isAccessTokenExpired()).thenReturn(false);
        //when
        String result = kakaoTokenService.getValidAccessTokenInServer(email);

        //then
        assertEquals(kakaoToken.getAccessToken(), result);
    }

    @Test
    @DisplayName("getValidAccessTokenInServer 메소드 테스트 - accessToken 및 refreshToken 만료 후")
    void getValidAccessTokenInServerTestWhenAccessTokenAndRefreshTokenIsExpired() {
        //given
        String email = "test@email.com";
        KakaoToken kakaoToken = mock(KakaoToken.class);

        when(kakaoTokenRepository.findByMemberEmail(email)).thenReturn(Optional.of(kakaoToken));
        when(kakaoToken.isAccessTokenExpired()).thenReturn(true);
        when(kakaoToken.isRefreshTokenExpired()).thenReturn(true);

        //when, then
        assertThrows(InvalidJwtException.class, () -> kakaoTokenService.getValidAccessTokenInServer(email));
    }
}
