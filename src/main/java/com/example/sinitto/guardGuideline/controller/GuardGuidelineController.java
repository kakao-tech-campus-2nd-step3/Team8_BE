package com.example.sinitto.guardGuideline.controller;

import com.example.sinitto.guardGuideline.dto.GuardGuidelineRequest;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/guardguidelines")
@Tag(name = "[미구현][의논필요]보호자용 가이드라인", description = "보호자가 입력하는 시니어별 가이드라인 관련 API")
public class GuardGuidelineController {

    @Operation(summary = "가이드라인 추가", description = "보호자가 시니어별 가이드라인을 추가합니다.")
    @PostMapping
    public ResponseEntity<String> addGuideline(@RequestBody GuardGuidelineRequest guardGuidelineRequest) {
        // 임시 응답
        return ResponseEntity.ok("가이드라인이 추가되었습니다.");
    }

    @Operation(summary = "카테고리에 해당하는 모든 가이드라인 조회", description = "시니또용 앱에서 카테고리에 해당하는 모든 가이드라인들을 요청할 때 필요합니다.")
    @GetMapping("/{seniorId}/{typeId}")
    public ResponseEntity<List<GuardGuidelineResponse>> getGuidelinesByCategory(@PathVariable Long seniorId, @PathVariable Long typeId) {
        // 임시 응답
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Operation(summary = "가이드라인 수정", description = "보호자가 특정 가이드라인을 수정할 때 필요합니다.")
    @PutMapping("/{guidelineId}")
    public ResponseEntity<String> updateGuideline(@PathVariable Long guidelineId, @RequestBody GuardGuidelineRequest guardGuidelineRequest) {
        // 임시 응답
        return ResponseEntity.ok("가이드라인이 수정되었습니다.");
    }

    @Operation(summary = "모든 가이드라인 조회(시니어별로)", description = "보호자가 가이드라인 수정을 위해 시니어별로 모든 가이드라인을 요청할 때 필요합니다.")
    @GetMapping("/{seniorId}")
    public ResponseEntity<List<GuardGuidelineResponse>> getAllGuidelinesBySenior(@PathVariable Long seniorId) {
        // 임시 응답
        return ResponseEntity.ok(new ArrayList<>());
    }

    @Operation(summary = "특정 가이드라인 조회", description = "보호자용 API입니다.")
    @GetMapping("/{guidelineId}")
    public ResponseEntity<GuardGuidelineResponse> getGuideline(@PathVariable Long guidelineId) {
        // 임시 응답
        return ResponseEntity.ok(new GuardGuidelineResponse(null, null));
    }

}
