package com.example.sinitto.member.service;

import com.example.sinitto.auth.service.TokenService;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberTokenService {
    private final TokenService tokenService;
    private final MemberRepository memberRepository;

    public MemberTokenService(TokenService tokenService, MemberRepository memberRepository){
        this.tokenService = tokenService;
        this.memberRepository = memberRepository;
    }

    public Long getMemberIdByToken(String token) {
        String email = tokenService.extractEmailFromAccessToken(token);
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("이메일에 해당하는 멤버를 찾을 수 없습니다.")
        );
        return member.getId();
    }
}
