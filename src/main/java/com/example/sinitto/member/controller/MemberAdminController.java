package com.example.sinitto.member.controller;

import com.example.sinitto.auth.service.TokenService;
import com.example.sinitto.common.properties.DummyProperties;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping
public class MemberAdminController {

    private final MemberRepository memberRepository;
    private final TokenService tokenService;
    private final DummyProperties dummyProperties;
    private final String adminEmail = "admin@kakao.com";
    private final String adminPassword = "1234";
    private final List<String> dummyEmails = Arrays.asList(
            "1chulsoo@example.com", "2kim@example.com", "3lee@example.com", "4park@example.com", "5choi@example.com",
            "6jeong@example.com", "7han@example.com", "8oh@example.com", "9lim@example.com", "10song@example.com"
    );

    public MemberAdminController(MemberRepository memberRepository, TokenService tokenService, DummyProperties dummyProperties) {
        this.memberRepository = memberRepository;
        this.tokenService = tokenService;
        this.dummyProperties = dummyProperties;
    }

    @GetMapping("/dummy")
    public String showDummyLoginPage(Model model) {
        List<Member> dummyMembers = memberRepository.findAllByEmailIn(dummyEmails);
        model.addAttribute("members", dummyMembers);
        return "dummy/login";
    }

    @GetMapping("/admin/login")
    public String showAdminLoginPage(HttpSession session) {
        if (isAdmin(session)) { return "redirect:/admin/point/charge"; }
        return "point/login";
    }

    @PostMapping("/admin/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session) {
        if (adminEmail.equals(email) && adminPassword.equals(password)) {
            session.setAttribute("email", email);
            session.setAttribute("role", "ADMIN");
            session.setMaxInactiveInterval(1800);
            return "redirect:/admin/point/charge";
        } else {
            return "redirect:/admin/login?error=true";
        }
    }

    @PostMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @PostMapping("/dummy")
    public String login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("env") String env,
            Model model
    ) {
        List<Member> dummyMembers = memberRepository.findAllByEmailIn(dummyEmails);
        model.addAttribute("members", dummyMembers);

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

    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equals(role);
    }
}
