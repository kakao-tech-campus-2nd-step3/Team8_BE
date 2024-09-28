package com.example.sinitto.member.controller;

import com.example.sinitto.member.dto.SignupRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원가입", description = "회원가입 시 정보 입력 관련 API")
public class MemberController {

    @Operation(summary = "시니또 회원가입", description = "시니또의 회원가입")
    @PostMapping("/sinitto")
    public ResponseEntity<String> sinittoSignup(@RequestBody SignupRequest request) {
        // 임시 응답
        return ResponseEntity.ok("시니또 회원가입이 완료되었습니다.");
    }

    @Operation(summary = "보호자 회원가입", description = "보호자의 회원가입")
    @PostMapping("/guard")
    public ResponseEntity<String> guardSignup(@RequestBody SignupRequest request) {
        // 임시 응답
        return ResponseEntity.ok("보호자 회원가입이 완료되었습니다.");
    }
}
