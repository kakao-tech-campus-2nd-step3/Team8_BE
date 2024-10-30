package com.example.sinitto.member.controller;

import com.example.sinitto.auth.service.TokenService;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/dummy")
public class MemberAdminController {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;

    public MemberAdminController(MemberRepository memberRepository, TokenService tokenService) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
    }

    @GetMapping
    public String showDummyLoginPage() {
        return "dummy/login";
    }

    @PostMapping
    public String login(@RequestParam("email") String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("해당 이메일을 가진 멤버를 찾을 수 없습니다."));

        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);

        String frontendRedirectUrl = "http://localhost:5173/dummy.html";
        return "redirect:" + frontendRedirectUrl + "?accessToken=" + accessToken + "&refreshToken=" + refreshToken;
    }
}
