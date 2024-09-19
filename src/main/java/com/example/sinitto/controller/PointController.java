package com.example.sinitto.controller;

import com.example.sinitto.dto.PointLogResponse;
import com.example.sinitto.dto.PointRequest;
import com.example.sinitto.dto.PointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/points")
@Tag(name = "[미구현]포인트", description = "포인트 API")
public class PointController {

    @Operation(summary = "포인트 추가", description = "(임시) 관리자가 직접 추가 - 이후 외부 결제 api 연동")
    @PutMapping("/add")
    public ResponseEntity<String> addPoints(@RequestBody PointRequest request) {
        // 임시 응답
        return ResponseEntity.ok("포인트가 추가되었습니다.");
    }

    @Operation(summary = "포인트 차감", description = "요청사항 등록 시 포인트를 차감합니다.")
    @PutMapping("/deduct")
    public ResponseEntity<String> deductPoints(@RequestBody PointRequest request) {
        // 임시 응답
        return ResponseEntity.ok("포인트가 차감되었습니다.");
    }

    @Operation(summary = "포인트 조회", description = "시니또, 보호자가 본인의 포인트를 조회합니다.")
    @GetMapping()
    public ResponseEntity<PointResponse> getPoint() {
        // @MemberId 같은걸로 멤버 특정해서 포인트 가지고옴
        return ResponseEntity.ok(new PointResponse());
    }

    @Operation(summary = "포인트 출금 요청", description = "시니또가 포인트 출금을 요청합니다.")
    @PostMapping("/withdraw")
    public ResponseEntity<String> requestPointWithdraw(@RequestBody PointRequest request) {
        // 임시 응답
        return ResponseEntity.ok("포인트 출금 요청이 완료되었습니다.");
    }

    @Operation(summary = "포인트 로그 조회", description = "포인트 로그를 조회합니다.")
    @GetMapping("/logs")
    public ResponseEntity<List<PointLogResponse>> getPointLogs() {
        // 임시 응답
        PointLogResponse response;
        return ResponseEntity.ok(new ArrayList<>());
    }

}
