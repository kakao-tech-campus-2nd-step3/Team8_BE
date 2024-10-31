package com.example.sinitto.member.controller;

import com.example.sinitto.auth.service.TokenService;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.common.properties.DummyProperties;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/dummy")
public class MemberAdminController {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final DummyProperties dummyProperties;

    public MemberAdminController(MemberRepository memberRepository, TokenService tokenService, DummyProperties dummyProperties) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
        this.dummyProperties = dummyProperties;
    }

    @GetMapping
    public String showDummyLoginPage() {
        return "dummy/login";
    }

    @PostMapping
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("env") String env,
            Model model
    ) {
        if (!password.equals(dummyProperties.password())) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "dummy/login";
        }

        Member member = memberRepository.findByEmail(email).orElse(null);
        if (member == null) {
            model.addAttribute("errorMessage", "해당 이메일을 가진 멤버를 찾을 수 없습니다. 데이터베이스에서 삭제되었는지 확인해주세요.");
            return "dummy/login";
        }
        String accessToken = tokenService.generateAccessToken(email);
        String refreshToken = tokenService.generateRefreshToken(email);
        boolean isSinitto = member.isSinitto();

        String frontendRedirectUrl = env.equals("dev") ? dummyProperties.devRedirectUri() : dummyProperties.redirectUri();
        return "redirect:" + frontendRedirectUrl + "?accessToken=" + accessToken + "&refreshToken=" + refreshToken + "&isSinitto=" + isSinitto;
    }
}
