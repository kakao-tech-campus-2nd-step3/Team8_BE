package com.example.sinitto.helloCall.controller;

import com.example.sinitto.helloCall.dto.HelloCallDetailResponse;
import com.example.sinitto.helloCall.dto.HelloCallReportRequest;
import com.example.sinitto.helloCall.dto.HelloCallResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/hellocalls")
@Tag(name = "[미구현]안부전화", description = "안부 전화 서비스 관련 API")
public class HelloCallController {

    @Operation(summary = "안부 전화 서비스 전체 리스트 보기", description = "보호자가 요청한 안부전화 신청정보를 리스트로 조회합니다. 페이징 처리 됩니다.")
    @GetMapping
    public ResponseEntity<Page<HelloCallResponse>> getHelloCallList() {
        // 임시 응답
        return ResponseEntity.ok(new PageImpl<>(new ArrayList<>()));
    }

    @Operation(summary = "선택한 안부 전화 서비스의 상세정보 보기", description = "안부전화 신청정보의 상세정보를 조회합니다.")
    @GetMapping("/{callId}")
    public ResponseEntity<HelloCallDetailResponse> getHelloCallDetail(@PathVariable Long callId) {
        // 임시 응답
        return ResponseEntity.ok(new HelloCallDetailResponse(null, null, null, null, null, null));
    }

    @Operation(summary = "서비스 수락하기", description = "시니또가 안부전화 신청을 수락합니다.")
    @PutMapping("/accept/{callId}")
    public ResponseEntity<String> acceptHelloCall(@PathVariable Long callId) {
        // 임시 응답
        return ResponseEntity.ok("안부전화 서비스가 수락되었습니다.");
    }

    @Operation(summary = "소통 보고서 작성", description = "시니또가 안부전화 후에 보고서를 작성합니다.")
    @PostMapping("/reports")
    public ResponseEntity<String> createHelloCallReport(@RequestBody HelloCallReportRequest request) {
        // 임시 응답
        return ResponseEntity.ok("소통 보고서가 작성되었습니다.");
    }

    @Operation(summary = "서비스 수행 완료", description = "시니또가 안부전화 수행을 완료합니다.")
    @PutMapping("/complete/{callId}")
    public ResponseEntity<String> completeHelloCall(@PathVariable Long callId) {
        // 임시 응답
        return ResponseEntity.ok("안부전화 서비스가 완료되었습니다.");
    }

    @Operation(summary = "소통 보고서  조회", description = "보호자가 보고서  조회합니다.")
    @GetMapping("/reports")
    public ResponseEntity<HelloCallDetailResponse> getHelloCallReports() {
        // 임시 응답
        return ResponseEntity.ok(new HelloCallDetailResponse(null, null, null, null, null, null));
    }

    @Operation(summary = "소통 보고서 상세조회", description = "보호자가 보고서 상세정보를 조회합니다.")
    @GetMapping("/reports/{reportId}")
    public ResponseEntity<HelloCallDetailResponse> getHelloCallReportDetail(@PathVariable Long reportId) {
        // 임시 응답
        return ResponseEntity.ok(new HelloCallDetailResponse(null, null, null, null, null, null));
    }

    @Operation(summary = "진행중인 안부 서비스 취소 요청", description = "시니또가 진행중인 안부전화 서비스를 취소합니다.")
    @PutMapping("/cancel/{callId}")
    public ResponseEntity<String> cancelHelloCall(@PathVariable Long callId) {
        // 임시 응답
        return ResponseEntity.ok("안부전화 서비스가 취소되었습니다.");
    }

}
