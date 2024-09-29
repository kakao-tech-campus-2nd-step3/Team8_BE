package com.example.sinitto.auth.repository;

import com.example.sinitto.auth.entity.KakaoToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class KakaoTokenRepositoryTest {

    @Autowired
    private KakaoTokenRepository kakaoTokenRepository;

    @Test
    @DisplayName("카카오 토큰 저장 테스트")
    void saveTest() {
        KakaoToken expected = new KakaoToken(
                "test@test.com",
                "access_token_123",
                "refresh_token_123",
                3600,
                7200
        );
        KakaoToken actual = kakaoTokenRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getMemberEmail()).isEqualTo(expected.getMemberEmail());
    }

    @Test
    @DisplayName("이메일 기반으로 카카오 토큰 찾기 테스트")
    void findByEmailTest() {
        KakaoToken kakaoToken = new KakaoToken(
                "test2@test.com",
                "access_token_456",
                "refresh_token_456",
                3600,
                7200
        );
        KakaoToken actual = kakaoTokenRepository.save(kakaoToken);
        Optional<KakaoToken> expected = kakaoTokenRepository.findByMemberEmail(kakaoToken.getMemberEmail());

        assertThat(actual.getId()).isNotNull();
        assertThat(expected).isPresent();
        assertThat(expected.get().getAccessToken()).isEqualTo(kakaoToken.getAccessToken());
    }

    @Test
    @DisplayName("저장되지 않은 이메일로 카카오 토큰을 찾을 때 빈 Optional을 리턴하는지 테스트")
    void edgeCaseTest() {
        String notSavedEmail = "notSavedEmail@test.com";

        assertThat(kakaoTokenRepository.findByMemberEmail(notSavedEmail)).isEmpty();
    }
}
