package com.example.sinitto.member.service;

import com.example.sinitto.common.resolver.MemberIdProvider;
import com.example.sinitto.member.dto.KakaoTokenResponse;
import com.example.sinitto.member.dto.KakaoUserResponse;
import com.example.sinitto.member.dto.LoginResponse;
import com.example.sinitto.member.dto.RegisterResponse;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.exception.NotUniqueException;
import com.example.sinitto.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService implements MemberIdProvider {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final KakaoApiService kakaoApiService;

    public MemberService(MemberRepository memberRepository, TokenService tokenService, KakaoApiService kakaoApiService) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
        this.kakaoApiService = kakaoApiService;
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

        if (memberRepository.findByEmail(email).isEmpty()) {
            return new LoginResponse(null, null, "/signup", email);
        }

        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);
        return new LoginResponse(accessToken, refreshToken, null, null);
    }

    public RegisterResponse registerNewMember(String name, String phoneNumber, String email, boolean isSinitto) {

        if (memberRepository.findByEmail(email).isPresent()) {
            throw new NotUniqueException("이미 존재하는 이메일입니다.");
        }

        Member newMember = new Member(name, phoneNumber, email, isSinitto);
        memberRepository.save(newMember);

        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);

        return new RegisterResponse(accessToken, refreshToken);
    }
}
