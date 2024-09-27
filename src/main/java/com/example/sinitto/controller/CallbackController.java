package com.example.sinitto.controller;

import com.example.sinitto.dto.CallbackResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/callbacks")
@Tag(name = "[미구현]콜백 서비스", description = "콜백 관련 API")
public class CallbackController {

    @Operation(summary = "콜백 전화 리스트 보기(페이지)", description = "시니어가 요청한 콜백전화를 페이징으로 보여줍니다.")
    @GetMapping
    public ResponseEntity<Page<CallbackResponse>> getCallbackList() {
        // 임시 응답
        return ResponseEntity.ok(new PageImpl<>(new ArrayList<>()));
    }

    @Operation(summary = "콜백 전화 상세보기(not 페이징)", description = "")
    @GetMapping("/{callbackId}")
    public ResponseEntity<CallbackResponse> getCallbackGuidelines(@PathVariable Long callbackId) {
        // 임시 응답
        return ResponseEntity.ok(new CallbackResponse());
    }

    @Operation(summary = "콜백 전화 완료", description = "시니또와 시니어의 연락이 끝났을 때 시니어의 요청사항을 수행 여부를 선택하여 처리합니다.")
    @PutMapping("/complete/{callbackId}")
    public ResponseEntity<String> completeCallback(@PathVariable Long callbackId) {
        // 임시 응답
        return ResponseEntity.ok("콜백 전화 끝냄");
    }

    @Operation(summary = "콜백 서비스 수락 신청", description = "시니또가 콜백 서비스 수락을 신청합니다.")
    @PutMapping("/accept/{callbackId}")
    public ResponseEntity<String> acceptCallbackService(@PathVariable Long callbackId) {
        return ResponseEntity.ok("콜백 서비스가 수락");
    }

    @Operation(summary = "진행중인 콜백 서비스 취소", description = "시니또가 진행중인 콜백 서비스를 취소합니다.")
    @PutMapping("/cancel/{callbackId}")
    public ResponseEntity<String> cancelCallbackService(@PathVariable Long callbackId) {
        // 임시 응답
        return ResponseEntity.ok("콜백 서비스가 취소");
    }

}
