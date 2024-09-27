package com.example.sinitto.member.service;

import com.example.sinitto.member.dto.KakaoTokenResponse;
import com.example.sinitto.member.entity.KakaoToken;
import com.example.sinitto.member.exception.KakaoRefreshTokenExpirationException;
import com.example.sinitto.member.exception.TokenNotFoundException;
import com.example.sinitto.member.repository.KakaoTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class KakaoTokenSerivce {

    private final KakaoApiService kakaoApiService;
    private final KakaoTokenRepository kakaoTokenRepository;

    public KakaoTokenSerivce(KakaoApiService kakaoApiService, KakaoTokenRepository kakaoTokenRepository) {
        this.kakaoApiService = kakaoApiService;
        this.kakaoTokenRepository = kakaoTokenRepository;
    }

    public void saveKakaoToken(String email, KakaoTokenResponse kakaoTokenResponse) {

        KakaoToken kakaoToken = new KakaoToken(email, kakaoTokenResponse.accessToken(),
                kakaoTokenResponse.refreshToken(), kakaoTokenResponse.expiresIn(), kakaoTokenResponse.refreshTokenExpiresIn());

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
