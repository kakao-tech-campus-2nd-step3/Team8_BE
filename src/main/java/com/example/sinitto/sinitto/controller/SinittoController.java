package com.example.sinitto.sinitto.controller;

import com.example.sinitto.common.annotation.MemberId;
import com.example.sinitto.sinitto.dto.SinittoBankRequest;
import com.example.sinitto.sinitto.dto.SinittoRequest;
import com.example.sinitto.sinitto.dto.SinittoResponse;
import com.example.sinitto.sinitto.service.SinittoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sinittos")
@Tag(name = "마이페이지 - 시니또용", description = "시니또 관련 마이페이지 API")
public class SinittoController {

    private final SinittoService sinittoService;

    public SinittoController(SinittoService sinittoService) {
        this.sinittoService = sinittoService;
    }

    @Operation(summary = "시니또 본인 정보 조회", description = "시니또 본인의 정보를 요청한다.")
    @GetMapping
    public ResponseEntity<SinittoResponse> getSinittoInfo(@MemberId Long memberId) {
        return ResponseEntity.ok(sinittoService.readSinitto(memberId));
    }

    @Operation(summary = "계좌정보 등록", description = "시니또가 계좌정보 등록합니다.")
    @PostMapping("/bank")
    public ResponseEntity<String> createSeniorBankInfo(@MemberId Long memberId, @RequestBody SinittoBankRequest sinittoBankRequest) {
        sinittoService.createSinittoBankInfo(memberId, sinittoBankRequest);
        return ResponseEntity.ok("계좌등록되었습니다.");
    }

    @Operation(summary = "시니또 본인 정보 수정", description = "시니또 본인의 정보를 수정요청한다.")
    @PutMapping
    public ResponseEntity<String> updateSinitto(@MemberId Long memberId, @RequestBody SinittoRequest sinittoRequest) {
        sinittoService.updateSinitto(memberId, sinittoRequest);
        return ResponseEntity.ok("시니또 정보가 수정되었습니다.");
    }

    @Operation(summary = "시니또 계좌 정보 수정", description = "시니또 본인의 정보를 수정요청한다.")
    @PutMapping("/bank")
    public ResponseEntity<String> updateSinittoBankInfo(@MemberId Long memberId, @RequestBody SinittoBankRequest sinittoBankRequest) {
        sinittoService.updateSinittoBankInfo(memberId, sinittoBankRequest);
        return ResponseEntity.ok("시니또 정보가 수정되었습니다.");
    }

    @Operation(summary = "시니또 삭제", description = "관리자용")
    @DeleteMapping
    public ResponseEntity<String> deleteSinitto(@MemberId Long memberId) {
        sinittoService.deleteSinitto(memberId);
        return ResponseEntity.ok("시니또가 삭제되었습니다.");
    }

    @Operation(summary = "시니또 계좌정보 삭제", description = "관리자용")
    @DeleteMapping("/bank")
    public ResponseEntity<String> deleteSinittoBankInfo(@MemberId Long memberId) {
        sinittoService.deleteSinittoBankInfo(memberId);
        return ResponseEntity.ok("시니또 계좌정보가 삭제되었습니다.");
    }

    @Operation(summary = "모든 시니또 조회", description = "관리자용")
    @GetMapping("/all")
    public ResponseEntity<List<SinittoResponse>> getAllSinittos() {
        return ResponseEntity.ok(sinittoService.readAllSinitto());
    }
}
