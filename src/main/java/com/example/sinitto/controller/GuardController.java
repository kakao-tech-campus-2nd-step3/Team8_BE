package com.example.sinitto.controller;

import com.example.sinitto.dto.GuardRequest;
import com.example.sinitto.dto.GuardResponse;
import com.example.sinitto.dto.SeniorRequest;
import com.example.sinitto.dto.SeniorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/guards")
@Tag(name = "[미구현]마이페이지 - 보호자용", description = "보호자와 시니어 관련 마이페이지 API")
public class GuardController {

    @Operation(summary = "연결된 모든 시니어 정보 조회", description = "보호자가 등록한 모든 시니어의 정보를 요청합니다.")
    @GetMapping("/senior")
    public ResponseEntity<List<SeniorResponse>> getAllSeniors() {
        // 임시 응답
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Operation(summary = "연결된 특정 시니어 정보 조회", description = "보호자가 등록한 특정 시니어의 정보를 요청합니다.")
    @GetMapping("/senior/{seniorId}")
    public ResponseEntity<SeniorResponse> getSenior(@PathVariable Long seniorId) {
        // 임시 응답
        return ResponseEntity.ok(new SeniorResponse(null, null, null));
    }

    @Operation(summary = "시니어 정보 수정", description = "시니어의 정보를 수정합니다.")
    @PutMapping("/senior/{seniorId}")
    public ResponseEntity<String> updateSenior(@PathVariable Long seniorId, @RequestBody SeniorRequest request) {
        // 임시 응답
        return ResponseEntity.ok("시니어 정보가 수정되었습니다.");
    }

    @Operation(summary = "시니어 추가", description = "보호자가 새로운 시니어를 등록합니다.")
    @PostMapping("/senior")
    public ResponseEntity<String> createSenior(@RequestBody SeniorRequest request) {
        // 임시 응답
        return ResponseEntity.ok("새로운 시니어가 등록되었습니다.");
    }

    @Operation(summary = "시니어 삭제", description = "보호자가 시니어를 등록 해제합니다.")
    @DeleteMapping("/senior/{seniorId}")
    public ResponseEntity<String> deleteSenior(@PathVariable Long seniorId) {
        // 임시 응답
        return ResponseEntity.ok("시니어가 삭제되었습니다.");
    }

    @Operation(summary = "보호자 본인 정보 조회", description = "보호자의 본인 정보를 조회합니다.")
    @GetMapping
    public ResponseEntity<GuardResponse> getGuardInfo() {
        // 임시 응답
        return ResponseEntity.ok(new GuardResponse(null, null, null));
    }

    @Operation(summary = "보호자 본인 정보 수정", description = "보호자의 본인 정보를 수정합니다.")
    @PutMapping
    public ResponseEntity<String> updateGuardInfo(@RequestBody GuardRequest request) {
        // 임시 응답
        return ResponseEntity.ok("보호자 정보가 수정되었습니다.");
    }

    //PASS
    @Operation(summary = "보호자 삭제", description = "관리자용 API입니다.")
    @DeleteMapping
    public ResponseEntity<String> deleteGuard() {
        // 임시 응답
        return ResponseEntity.ok("보호자가 삭제되었습니다.");
    }

    @Operation(summary = "모든 보호자 조회", description = "관리자용 API입니다.")
    @GetMapping("/all")
    public ResponseEntity<List<GuardResponse>> getAllGuards() {
        // 임시 응답
        return ResponseEntity.ok(new ArrayList<>());
    }

}
