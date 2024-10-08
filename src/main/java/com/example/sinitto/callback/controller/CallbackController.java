package com.example.sinitto.callback.controller;

import com.example.sinitto.callback.dto.CallbackResponse;
import com.example.sinitto.callback.service.CallbackService;
import com.example.sinitto.common.annotation.MemberId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/callbacks")
@Tag(name = "콜백 서비스", description = "콜백 관련 API")
public class CallbackController {

    private final CallbackService callbackService;

    public CallbackController(CallbackService callbackService) {
        this.callbackService = callbackService;
    }

    @Operation(summary = "콜백 전화 리스트 보기(페이지)", description = "시니어가 요청한 콜백전화를 페이징으로 보여줍니다.")
    @GetMapping
    public ResponseEntity<Page<CallbackResponse>> getCallbackList(@MemberId Long memberId,
                                                                  @PageableDefault(sort = "postTime", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(callbackService.getCallbacks(memberId, pageable));
    }

    @Operation(summary = "콜백 전화 완료", description = "시니또와 시니어의 연락이 끝났을 때 시니어의 요청사항을 수행 여부를 선택하여 처리합니다.")
    @PutMapping("/complete/{callbackId}")
    public ResponseEntity<Void> completeCallback(@MemberId Long memberId,
                                                 @PathVariable Long callbackId) {

        callbackService.complete(memberId, callbackId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "콜백 서비스 수락 신청", description = "시니또가 콜백 서비스 수락을 신청합니다.")
    @PutMapping("/accept/{callbackId}")
    public ResponseEntity<Void> acceptCallback(@MemberId Long memberId,
                                               @PathVariable Long callbackId) {

        callbackService.accept(memberId, callbackId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "진행중인 콜백 서비스 취소", description = "시니또가 진행중인 콜백 서비스를 취소합니다. 콜백은 다시 대기 상태로 돌아갑니다.")
    @PutMapping("/cancel/{callbackId}")
    public ResponseEntity<Void> cancelCallback(@MemberId Long memberId,
                                               @PathVariable Long callbackId) {

        callbackService.cancel(memberId, callbackId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/twilio")
    public ResponseEntity<String> addCallCheck(@RequestParam("From") String fromNumber) {

        return ResponseEntity.ok(callbackService.add(fromNumber));
    }
}
