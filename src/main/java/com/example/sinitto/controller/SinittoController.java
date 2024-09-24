package com.example.sinitto.controller;

import com.example.sinitto.dto.SinittoRequest;
import com.example.sinitto.dto.SinittoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sinittos")
@Tag(name = "[미구현]마이페이지 - 시니또용", description = "시니또 관련 마이페이지 API")
public class SinittoController {

    @Operation(summary = "시니또 본인 정보 조회", description = "시니또 본인의 정보를 요청한다.")
    @GetMapping
    public ResponseEntity<SinittoResponse> getSinittoInfo() {
        // 임시 응답
        return ResponseEntity.ok(new SinittoResponse(null, null, null, null, null));
    }

    @Operation(summary = "시니또 본인 정보 수정", description = "시니또 본인의 정보를 수정요청한다.")
    @PutMapping
    public ResponseEntity<String> updateSinittoInfo(@RequestBody SinittoRequest request) {
        // 임시 응답
        return ResponseEntity.ok("시니또 정보가 수정되었습니다.");
    }

    //Pass
    @Operation(summary = "시니또 삭제", description = "관리자용")
    @DeleteMapping
    public ResponseEntity<String> deleteSinitto() {
        // 임시 응답
        return ResponseEntity.ok("시니또가 삭제되었습니다.");
    }

    //Pass
    @Operation(summary = "모든 시니또 조회", description = "관리자용")
    @GetMapping("/all")
    public ResponseEntity<List<SinittoResponse>> getAllSinittos() {
        // 임시 응답
        return ResponseEntity.ok(new ArrayList<>());
    }
}
