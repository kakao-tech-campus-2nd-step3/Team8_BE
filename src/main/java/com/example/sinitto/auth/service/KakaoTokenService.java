package com.example.sinitto.auth.service;

import com.example.sinitto.auth.dto.KakaoTokenResponse;
import com.example.sinitto.auth.entity.KakaoToken;
import com.example.sinitto.auth.exception.KakaoRefreshTokenExpirationException;
import com.example.sinitto.auth.exception.TokenNotFoundException;
import com.example.sinitto.auth.repository.KakaoTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class KakaoTokenService {

    private final KakaoApiService kakaoApiService;
    private final KakaoTokenRepository kakaoTokenRepository;

    public KakaoTokenService(KakaoApiService kakaoApiService, KakaoTokenRepository kakaoTokenRepository) {
        this.kakaoApiService = kakaoApiService;
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    @Transactional
    public void saveKakaoToken(String email, KakaoTokenResponse kakaoTokenResponse) {

        KakaoToken kakaoToken = kakaoTokenRepository.findByMemberEmail(email)
                .orElseGet(() -> new KakaoToken(email, kakaoTokenResponse.accessToken(),
                        kakaoTokenResponse.refreshToken(), kakaoTokenResponse.expiresIn(),
                        kakaoTokenResponse.refreshTokenExpiresIn()));

        kakaoToken.updateKakaoToken(kakaoTokenResponse.accessToken(), kakaoTokenResponse.refreshToken(),
                kakaoTokenResponse.expiresIn(), kakaoTokenResponse.refreshTokenExpiresIn());

        kakaoTokenRepository.save(kakaoToken);
    }

    @Transactional
    public String getValidAccessTokenInServer(String email) {
        KakaoToken kakaoToken = kakaoTokenRepository.findByMemberEmail(email)
                .orElseThrow(() -> new TokenNotFoundException("email에 해당하는 카카오 토큰이 없습니다."));

        if (kakaoToken.isAccessTokenExpired()) {
            if (kakaoToken.isRefreshTokenExpired()) {
                throw new KakaoRefreshTokenExpirationException("카카오 리프레쉬 토큰이 만료되었습니다. 카카오 재 로그인 필요");
            }
            KakaoTokenResponse kakaoTokenResponse = kakaoApiService.refreshAccessToken(kakaoToken.getRefreshToken());
            kakaoToken.updateKakaoToken(kakaoTokenResponse.accessToken(), kakaoTokenResponse.refreshToken(), kakaoTokenResponse.expiresIn(), kakaoTokenResponse.refreshTokenExpiresIn());
        }

        return kakaoToken.getAccessToken();
    }


}
