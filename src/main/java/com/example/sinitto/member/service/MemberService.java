package com.example.sinitto.member.service;

import com.example.sinitto.auth.dto.KakaoTokenResponse;
import com.example.sinitto.auth.dto.KakaoUserResponse;
import com.example.sinitto.auth.dto.LoginResponse;
import com.example.sinitto.auth.service.KakaoApiService;
import com.example.sinitto.auth.service.KakaoTokenService;
import com.example.sinitto.auth.service.TokenService;
import com.example.sinitto.common.resolver.MemberIdProvider;
import com.example.sinitto.member.dto.RegisterResponse;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.exception.NotUniqueException;
import com.example.sinitto.member.repository.MemberRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService implements MemberIdProvider {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final KakaoApiService kakaoApiService;
    private final KakaoTokenService kakaoTokenService;
    private final RedisTemplate<String, String> redisTemplate;


    public MemberService(MemberRepository memberRepository, TokenService tokenService, KakaoApiService kakaoApiService, KakaoTokenService kakaoTokenService
            ,RedisTemplate<String, String> redisTemplate) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
        this.kakaoApiService = kakaoApiService;
        this.kakaoTokenService = kakaoTokenService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Long getMemberIdByToken(String token) {
        String email = tokenService.extractEmail(token);
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new MemberNotFoundException("이메일에 해당하는 멤버를 찾을 수 없습니다.")
        );
        return member.getId();
    }

    public LoginResponse kakaoLogin(String authorizationCode) {
        KakaoTokenResponse kakaoTokenResponse = kakaoApiService.getAccessToken(authorizationCode);
        KakaoUserResponse kakaoUserResponse = kakaoApiService.getUserInfo(kakaoTokenResponse.accessToken());

        String email = kakaoUserResponse.kakaoAccount().email();

        kakaoTokenService.saveKakaoToken(email, kakaoTokenResponse);

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        if (optionalMember.isEmpty()) {
            return new LoginResponse(null, null, "/signup", email, false, false);
        }

        Member member = optionalMember.get();
        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);

        return new LoginResponse(accessToken, refreshToken, null, null, member.isSinitto(), true);
    }

    public RegisterResponse registerNewMember(String name, String phoneNumber, String email, boolean isSinitto) {

        if (memberRepository.existsByEmail(email)) {
            throw new NotUniqueException("이미 존재하는 이메일입니다.");
        }

        Member newMember = new Member(name, phoneNumber, email, isSinitto);
        memberRepository.save(newMember);

        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);

        return new RegisterResponse(accessToken, refreshToken, isSinitto);
    }

    public void memberLogout(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("id에 해당하는 멤버가 없습니다."));

        String storedRefreshToken = redisTemplate.opsForValue().get(member.getEmail());

        if (storedRefreshToken != null) {
            redisTemplate.delete(member.getEmail());
        }
    }
}
