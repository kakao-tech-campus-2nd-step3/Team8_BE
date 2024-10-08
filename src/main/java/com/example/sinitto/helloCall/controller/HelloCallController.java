package com.example.sinitto.helloCall.controller;

import com.example.sinitto.common.annotation.MemberId;
import com.example.sinitto.helloCall.dto.*;
import com.example.sinitto.helloCall.service.HelloCallPriceService;
import com.example.sinitto.helloCall.service.HelloCallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hellocalls")
@Tag(name = "안부전화", description = "안부 전화 서비스 관련 API")
public class HelloCallController {

    private final HelloCallService helloCallService;
    private final HelloCallPriceService helloCallPriceService;

    public HelloCallController(HelloCallService helloCallService, HelloCallPriceService helloCallPriceService) {
        this.helloCallService = helloCallService;
        this.helloCallPriceService = helloCallPriceService;
    }

    @Operation(summary = "[시니또용] 안부 전화 서비스 전체 리스트 보기", description = "안부전화 신청정보를 페이지로 조회합니다.")
    @GetMapping("/sinittos/list")
    public ResponseEntity<Page<HelloCallResponse>> getHelloCallListBySinitto(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<HelloCallResponse> helloCallResponses = helloCallService.readAllWaitingHelloCallsBySinitto(pageable);

        return ResponseEntity.ok(helloCallResponses);
    }

    @Operation(summary = "[보호자용] 보호자가 신청한 안부전화 리스트 보기", description = "보호자 본인이 신청한 안부전화 리스트를 조회합니다.")
    @GetMapping("/guards/lists")
    public ResponseEntity<List<HelloCallResponse>> getHelloCallListByGuard(@MemberId Long memberId) {

        List<HelloCallResponse> helloCallResponses = helloCallService.readAllHelloCallsByGuard(memberId);

        return ResponseEntity.ok(helloCallResponses);
    }

    @Operation(summary = "[시니또, 보호자용] 선택한 안부 전화 서비스의 상세정보 보기", description = "안부전화 신청정보의 상세정보를 조회합니다.")
    @GetMapping("/{callId}")
    public ResponseEntity<HelloCallDetailResponse> getHelloCallDetail(@PathVariable Long callId) {

        HelloCallDetailResponse helloCallDetailResponse = helloCallService.readHelloCallDetail(callId);

        return ResponseEntity.ok(helloCallDetailResponse);
    }

    @Operation(summary = "[보호자용] 안부 전화 서비스 비용 조회", description = "안부 전화 서비스의 이용 비용을 조회합니다.")
    @PostMapping("/guards/cost")
    public ResponseEntity<HelloCallPriceResponse> calculateHelloCallPrice(@RequestBody HelloCallPriceRequest helloCallPriceRequest) {

        HelloCallPriceResponse helloCallPriceResponse = helloCallPriceService.calculateHelloCallPrice(helloCallPriceRequest);

        return ResponseEntity.ok(helloCallPriceResponse);
    }

    @Operation(summary = "[보호자용] 안부 전화 서비스 신청하기", description = "보호자가 안부 전화 서비스를 신청합니다.")
    @PostMapping("/guards")
    public ResponseEntity<StringMessageResponse> createHelloCallByGuard(@MemberId Long memberId, @RequestBody HelloCallRequest helloCallRequest) {

        helloCallService.createHelloCallByGuard(memberId, helloCallRequest);

        return ResponseEntity.ok(new StringMessageResponse("안부 전화 서비스가 신청되었습니다."));
    }

    @Operation(summary = "[보호자용] 안부 전화 서비스 수정하기", description = "보호자가 안부 전화 서비스 내용을 수정합니다.")
    @PutMapping("/guards/{callId}")
    public ResponseEntity<StringMessageResponse> updateHelloCallByGuard(@MemberId Long memberId, @PathVariable Long callId, @RequestBody HelloCallDetailUpdateRequest helloCallDetailUpdateRequest) {

        helloCallService.updateHelloCallByGuard(memberId, callId, helloCallDetailUpdateRequest);

        return ResponseEntity.ok(new StringMessageResponse("안부 전화 서비스 내용이 수정되었습니다."));
    }

    @Operation(summary = "[보호자용] 안부 전화 서비스 삭제하기", description = "보호자가 안부 전화 서비스 신청을 취소합니다.")
    @DeleteMapping("/guards/{callId}")
    public ResponseEntity<StringMessageResponse> deleteHelloCallByGuard(@MemberId Long memberId, @PathVariable Long callId) {

        helloCallService.deleteHellCallByGuard(memberId, callId);

        return ResponseEntity.ok(new StringMessageResponse("신청된 안부전화 서비스가 삭제되었습니다."));
    }

    @Operation(summary = "[시니또용] 서비스 수락하기", description = "시니또가 안부전화 신청을 수락합니다.")
    @PutMapping("/accept/{callId}")
    public ResponseEntity<StringMessageResponse> acceptHelloCall(@MemberId Long memberId, @PathVariable Long callId) {

        helloCallService.acceptHelloCallBySinitto(memberId, callId);

        return ResponseEntity.ok(new StringMessageResponse("안부전화 서비스가 수락되었습니다."));
    }

    @Operation(summary = "[시니또용] 시니또가 수락한 안부전화 리스트 조회", description = "시니또가 수락한 안부전화 리스트를 조회합니다.")
    @GetMapping("/own")
    public ResponseEntity<List<HelloCallResponse>> readOwnHelloCallBySinitto(@MemberId Long memberId) {

        List<HelloCallResponse> helloCallResponses = helloCallService.readOwnHelloCallBySinitto(memberId);

        return ResponseEntity.ok(helloCallResponses);
    }

    @Operation(summary = "[시니또용] 안부전화 서비스 시작시간 기록", description = "시니또가 안부전화 시작시간을 기록합니다.")
    @PostMapping("/sinittos/start/{callId}")
    public ResponseEntity<StringMessageResponse> writeHelloCallStartTimeBySinitto(@MemberId Long memberId, @PathVariable Long callId) {

        helloCallService.writeHelloCallStartTimeBySinitto(memberId, callId);

        return ResponseEntity.ok(new StringMessageResponse("안부전화 시작 시간이 기록되었습니다."));
    }

    @Operation(summary = "[시니또용] 안부전화 서비스 종료시간 기록", description = "시니또가 안부전화 종료시간을 기록합니다.")
    @PostMapping("/sinittos/end/{callId}")
    public ResponseEntity<StringMessageResponse> writeHelloCallEndTimeBySinitto(@MemberId Long memberId, @PathVariable Long callId) {

        helloCallService.writeHelloCallEndTimeBySinitto(memberId, callId);

        return ResponseEntity.ok(new StringMessageResponse("안부전화 종료 시간이 기록되었습니다."));
    }

    @Operation(summary = "[시니또용] 소통 보고서 작성 및 완료 대기 상태 변경", description = "시니또가 최종 안부전화 후에 보고서를 작성합니다.")
    @PostMapping("/reports")
    public ResponseEntity<StringMessageResponse> createHelloCallReport(@MemberId Long memberId, @RequestBody HelloCallReportRequest request) {

        helloCallService.SendReportBySinitto(memberId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(new StringMessageResponse("소통 보고서가 작성되었습니다."));
    }

    @Operation(summary = "[보호자용] 완료 대기 상태 안부전화 완료 처리", description = "보호자가 완료 대기 상태인 안부전화의 상태를 완료로 변경합니다.")
    @PostMapping("/complete/{callId}")
    public ResponseEntity<StringMessageResponse> completeHelloCall(@MemberId Long memberId, @PathVariable Long callId) {

        helloCallService.makeCompleteHelloCallByGuard(memberId, callId);

        return ResponseEntity.ok(new StringMessageResponse("보호자에 의해 해당 안부전화가 완료처리 되었습니다."));
    }

    @Operation(summary = "[보호자용] 안부전화 타임로그 조회", description = "보호자가 안부전화를 수행한 시니또의 전화 타임로그를 리스트로 조회합니다.")
    @GetMapping("/guards/log/{callId}")
    public ResponseEntity<List<HelloCallTimeLogResponse>> readHelloCallTimeLogByGuard(@MemberId Long memberId, @PathVariable Long callId) {

        List<HelloCallTimeLogResponse> helloCallTimeLogResponses = helloCallService.readHelloCallTimeLogByGuard(memberId, callId);

        return ResponseEntity.ok(helloCallTimeLogResponses);
    }


    @Operation(summary = "[보호자용] 소통 보고서 조회", description = "보호자가 시니또가 작성한 보고서가 있다면 보고서를 조회합니다.")
    @GetMapping("/reports/{callId}")
    public ResponseEntity<HelloCallReportResponse> getHelloCallReportDetail(@MemberId Long memberId, @PathVariable Long callId) {

        HelloCallReportResponse helloCallReportResponse = helloCallService.readHelloCallReportByGuard(memberId, callId);

        return ResponseEntity.ok(helloCallReportResponse);
    }

    @Operation(summary = "[시니또용] 진행중인 안부 서비스 취소 요청", description = "시니또가 진행중인 안부전화 서비스를 취소합니다. 취소시 포인트는 받을 수 없습니다.")
    @PutMapping("/cancel/{callId}")
    public ResponseEntity<StringMessageResponse> cancelHelloCall(@MemberId Long memberId, @PathVariable Long callId) {

        helloCallService.cancelHelloCallBySinitto(memberId, callId);

        return ResponseEntity.ok(new StringMessageResponse("안부전화 서비스가 취소되었습니다."));
    }

    @Operation(summary = "[관리자용] 모든 안부전화 리포트 조회", description = "관리자가 모든 안부전화의 리포트를 조회합니다.")
    @GetMapping("/admin/reports")
    public ResponseEntity<List<HelloCallReportResponse>> readAllHelloCallReportByAdmin() {

        List<HelloCallReportResponse> helloCallReportResponses = helloCallService.readAllHelloCallReportByAdmin();

        return ResponseEntity.ok(helloCallReportResponses);
    }
}
