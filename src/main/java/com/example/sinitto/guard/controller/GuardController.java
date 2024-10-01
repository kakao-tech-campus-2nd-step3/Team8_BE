package com.example.sinitto.guard.controller;

import com.example.sinitto.common.annotation.MemberId;
import com.example.sinitto.guard.dto.GuardRequest;
import com.example.sinitto.guard.dto.GuardResponse;
import com.example.sinitto.guard.dto.SeniorRequest;
import com.example.sinitto.guard.dto.SeniorResponse;
import com.example.sinitto.guard.service.GuardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/guards")
@Tag(name = "마이페이지 - 보호자용", description = "보호자와 시니어 관련 마이페이지 API")
public class GuardController {
    private final GuardService guardService;

    public GuardController(GuardService guardService){
        this.guardService = guardService;
    }
    @Operation(summary = "연결된 모든 시니어 정보 조회", description = "보호자가 등록한 모든 시니어의 정보를 요청합니다.")
    @GetMapping("/senior")
    public ResponseEntity<List<SeniorResponse>> getAllSeniors(@MemberId Long memberId) {
        return ResponseEntity.ok(guardService.readSeniors(memberId));
    }

    @Operation(summary = "연결된 특정 시니어 정보 조회", description = "보호자가 등록한 특정 시니어의 정보를 요청합니다.")
    @GetMapping("/senior/{seniorId}")
    public ResponseEntity<SeniorResponse> getSenior(@MemberId Long memberId, @PathVariable Long seniorId) {
        return ResponseEntity.ok(guardService.readOneSenior(memberId, seniorId));
    }

    @Operation(summary = "시니어 정보 수정", description = "시니어의 정보를 수정합니다.")
    @PutMapping("/senior/{seniorId}")
    public ResponseEntity<String> updateSenior(@MemberId Long memberId, @PathVariable Long seniorId, @RequestBody SeniorRequest seniorRequest) {
        guardService.updateSenior(memberId, seniorId, seniorRequest);
        return ResponseEntity.ok("시니어 정보가 수정되었습니다.");
    }

    @Operation(summary = "시니어 추가", description = "보호자가 새로운 시니어를 등록합니다.")
    @PostMapping("/senior")
    public ResponseEntity<String> createSenior(@MemberId Long memberId, @RequestBody SeniorRequest seniorRequest) {
        guardService.createSenior(memberId, seniorRequest);
        return ResponseEntity.ok("새로운 시니어가 등록되었습니다.");
    }

    @Operation(summary = "시니어 삭제", description = "보호자가 시니어를 등록 해제합니다.")
    @DeleteMapping("/senior/{seniorId}")
    public ResponseEntity<String> deleteSenior(@MemberId Long memberId, @PathVariable Long seniorId) {
        guardService.deleteSenior(memberId, seniorId);
        return ResponseEntity.ok("시니어가 삭제되었습니다.");
    }

    @Operation(summary = "보호자 본인 정보 조회", description = "보호자의 본인 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<GuardResponse> getGuardInfo(@MemberId Long memberId) {
        return ResponseEntity.ok(guardService.readGuard(memberId));
    }

    @Operation(summary = "보호자 본인 정보 수정", description = "보호자의 본인 정보를 수정합니다.")
    @PutMapping
    public ResponseEntity<String> updateGuardInfo(@MemberId Long memberId, @RequestBody GuardRequest guardRequest) {
        guardService.updateGuard(memberId, guardRequest);
        return ResponseEntity.ok("보호자 정보가 수정되었습니다.");
    }

    // 현재는 jwt 안의 id를 삭제하게 구현했는데, 나중에 관리자 계정 만들면 특정 id 지정해서 삭제하게 수정해야할 듯합니다.
    @Operation(summary = "보호자 삭제", description = "관리자용 API입니다.")
    @DeleteMapping
    public ResponseEntity<String> deleteGuard(@MemberId Long memberId) {
        guardService.deleteGuard(memberId);
        return ResponseEntity.ok("보호자가 삭제되었습니다.");
    }

    @Operation(summary = "모든 보호자 조회", description = "관리자용 API입니다.")
    @GetMapping("/all")
    public ResponseEntity<List<GuardResponse>> getAllGuards(@MemberId Long memberId) {
        return ResponseEntity.ok(guardService.readAllGuards());
    }

}
