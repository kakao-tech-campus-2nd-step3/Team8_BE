package com.example.sinitto.member.service;

import com.example.sinitto.auth.dto.KakaoTokenResponse;
import com.example.sinitto.auth.dto.KakaoUserResponse;
import com.example.sinitto.auth.dto.LoginResponse;
import com.example.sinitto.auth.service.KakaoApiService;
import com.example.sinitto.auth.service.KakaoTokenService;
import com.example.sinitto.auth.service.TokenService;
import com.example.sinitto.callback.service.CallbackService;
import com.example.sinitto.common.exception.ConflictException;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.helloCall.service.HelloCallService;
import com.example.sinitto.member.dto.RegisterResponse;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.repository.PointRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService{

    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final KakaoApiService kakaoApiService;
    private final KakaoTokenService kakaoTokenService;
    private final PointRepository pointRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CallbackService callbackService;
    private final HelloCallService helloCallService;

    public MemberService(MemberRepository memberRepository, TokenService tokenService, KakaoApiService kakaoApiService, KakaoTokenService kakaoTokenService, PointRepository pointRepository, RedisTemplate<String, Object> redisTemplate, CallbackService callbackService, HelloCallService helloCallService) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
        this.kakaoApiService = kakaoApiService;
        this.kakaoTokenService = kakaoTokenService;
        this.pointRepository = pointRepository;
        this.redisTemplate = redisTemplate;
        this.callbackService = callbackService;
        this.helloCallService = helloCallService;
    }

    public LoginResponse kakaoLogin(String authorizationCode, HttpServletRequest httpServletRequest) {
        KakaoTokenResponse kakaoTokenResponse = kakaoApiService.getAccessToken(authorizationCode, httpServletRequest);
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
            throw new ConflictException("이미 존재하는 이메일입니다.");
        }

        Member newMember = new Member(name, phoneNumber, email, isSinitto);
        memberRepository.save(newMember);

        pointRepository.save(new Point(0, newMember));

        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);

        return new RegisterResponse(accessToken, refreshToken, isSinitto);
    }

    public void memberLogout(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(null);

        if (member == null) {
            return;
        }

        String storedRefreshToken = (String) redisTemplate.opsForHash().get(member.getEmail(), "refreshToken");

        if (storedRefreshToken != null) {
            redisTemplate.delete(member.getEmail());
        }
    }

    public void deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 멤버가 없습니다."));

        String storedRefreshToken = (String) redisTemplate.opsForHash().get(member.getEmail(), "refreshToken");

        if (storedRefreshToken != null) {
            redisTemplate.delete(member.getEmail());
        }

        if (member.isSinitto()) {
            callbackService.cancelAssignedCallbackIfInProgress(member);
            helloCallService.cancelAssignedHelloCallIfInProgress(member);
        }

        memberRepository.deleteById(memberId);
    }
}

