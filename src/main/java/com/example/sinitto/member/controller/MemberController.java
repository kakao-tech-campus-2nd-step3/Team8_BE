package com.example.sinitto.member.controller;

import com.example.sinitto.member.dto.RegisterResponse;
import com.example.sinitto.common.annotation.MemberId;
import com.example.sinitto.member.dto.SignupRequest;
import com.example.sinitto.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@Tag(name = "회원가입", description = "회원가입 시 정보 입력 관련 API")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @Operation(summary = "시니또 회원가입", description = "시니또의 회원가입", security = @SecurityRequirement(name = "JWT제외"))
    @PostMapping("/sinitto")
    public ResponseEntity<RegisterResponse> sinittoSignup(@RequestBody SignupRequest request) {
        RegisterResponse registerResponse = memberService.registerNewMember(request.name(), request.phoneNumber(),
                request.email(), request.isSinitto());
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @Operation(summary = "보호자 회원가입", description = "보호자의 회원가입", security = @SecurityRequirement(name = "JWT제외"))
    @PostMapping("/guard")
    public ResponseEntity<RegisterResponse> guardSignup(@RequestBody SignupRequest request) {
        RegisterResponse registerResponse = memberService.registerNewMember(request.name(), request.phoneNumber(),
                request.email(), request.isSinitto());
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    @Operation(summary = "멤버 로그아웃", description = "레디스에 저장되어있는 멤버의 refreshToken을 삭제합니다.")
    @DeleteMapping("/logout")
    public ResponseEntity<Void> memberLogout(@MemberId Long memberId) {
        memberService.memberLogout(memberId);
        return ResponseEntity.ok().build();
    }
}
