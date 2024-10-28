package com.example.sinitto.guardGuideline.controller;

import com.example.sinitto.common.annotation.MemberId;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineRequest;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineResponse;
import com.example.sinitto.guardGuideline.entity.GuardGuideline;
import com.example.sinitto.guardGuideline.service.GuardGuidelineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guardguidelines")
@Tag(name = "보호자용 가이드라인", description = "보호자가 입력하는 시니어별 가이드라인 관련 API")
public class GuardGuidelineController {

    private final GuardGuidelineService guardGuidelineService;

    public GuardGuidelineController(GuardGuidelineService guardGuidelineService) {this.guardGuidelineService = guardGuidelineService;}

    @Operation(summary = "가이드라인 추가", description = "보호자가 시니어별 가이드라인을 추가합니다.")
    @PostMapping
    public ResponseEntity<String> addGuardGuideline(@MemberId Long memberId, @RequestBody GuardGuidelineRequest guardGuidelineRequest) {
        guardGuidelineService.addGuardGuideline(memberId, guardGuidelineRequest);
        return ResponseEntity.ok("가이드라인이 추가되었습니다.");
    }

    @Operation(summary = "카테고리에 해당하는 모든 가이드라인 조회", description = "시니또용 앱에서 카테고리에 해당하는 모든 가이드라인들을 요청할 때 필요합니다.")
    @GetMapping("/{seniorId}/{type}")
    public ResponseEntity<List<GuardGuidelineResponse>> getGuardGuidelinesByCategory(@PathVariable Long seniorId, @PathVariable GuardGuideline.Type type) {
        return ResponseEntity.ok(guardGuidelineService.readAllGuardGuidelinesByCategory(seniorId, type));
    }

    @Operation(summary = "가이드라인 수정", description = "보호자가 특정 가이드라인을 수정할 때 필요합니다.")
    @PutMapping("/{guidelineId}")
    public ResponseEntity<String> updateGuardGuideline(@MemberId Long memberId, @PathVariable Long guidelineId, @RequestBody GuardGuidelineRequest guardGuidelineRequest) {
        guardGuidelineService.updateGuardGuideline(memberId, guidelineId, guardGuidelineRequest);
        return ResponseEntity.ok("가이드라인이 수정되었습니다.");
    }

    @Operation(summary = "모든 가이드라인 조회(시니어별로)", description = "보호자가 가이드라인 수정을 위해 시니어별로 모든 가이드라인을 요청할 때 필요합니다.")
    @GetMapping("/{seniorId}")
    public ResponseEntity<List<GuardGuidelineResponse>> getAllGuardGuidelinesBySenior(@PathVariable Long seniorId) {

        return ResponseEntity.ok(guardGuidelineService.readAllGuardGuidelinesBySenior(seniorId));
    }

    @Operation(summary = "특정 가이드라인 조회", description = "보호자용 API입니다.")
    @GetMapping("/{guidelineId}")
    public ResponseEntity<GuardGuidelineResponse> getGuardGuideline(@PathVariable Long guidelineId) {
        return ResponseEntity.ok(guardGuidelineService.readGuardGuideline(guidelineId));
    }

}
