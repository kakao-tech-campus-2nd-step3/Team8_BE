package com.example.sinitto.auth.controller;

import com.example.sinitto.auth.dto.TokenRefreshRequest;
import com.example.sinitto.auth.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "[미구현][의논필요]회원 인증", description = "회원 인증 관련 API")
public class AuthController {

    @Operation(summary = "토큰 재발급", description = "RefreshToken으로 AccessToken을 재발급 한다.")
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        // 임시 응답
        return ResponseEntity.ok(new TokenResponse());
    }

    @Operation(summary = "Oauth 카카오 인증페이지 리다이렉트", description = "카카오 로그인 화면으로 이동한다.")
    @GetMapping("/oauth/kakao")
    public ResponseEntity<String> redirectToKakaoAuth() {
        // 실제로는 리다이렉트 로직이 필요
        return ResponseEntity.ok("카카오 로그인 페이지로 리다이렉트됩니다.");
    }

    @Operation(summary = "Oauth 카카오 로그인 콜백", description = "카카오 로그인 이후 발생하는 인가코드를 통해 AccessToken을 발급한다.")
    @GetMapping("/oauth/kakao/callback")
    public ResponseEntity<TokenResponse> kakaoCallback(@RequestParam("code") String code) {
        // 임시 응답
        return ResponseEntity.ok(new TokenResponse());
    }

}
