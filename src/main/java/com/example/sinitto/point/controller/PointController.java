package com.example.sinitto.point.controller;

import com.example.sinitto.common.annotation.MemberId;
import com.example.sinitto.point.dto.PointChargeResponse;
import com.example.sinitto.point.dto.PointLogResponse;
import com.example.sinitto.point.dto.PointRequest;
import com.example.sinitto.point.dto.PointResponse;
import com.example.sinitto.point.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/points")
@Tag(name = "포인트", description = "포인트 API")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @Operation(summary = "포인트 조회", description = "시니또, 보호자가 본인의 포인트를 조회합니다.")
    @GetMapping()
    public ResponseEntity<PointResponse> getPoint(@MemberId Long memberId) {

        return ResponseEntity.ok(pointService.getPoint(memberId));
    }

    @Operation(summary = "포인트 충전 요청", description = "관리자가 직접 추가 - 바로 충전 되는거 아님. 신청->대기->완료 완료시점에 충전 됨")
    @PutMapping("/charge")
    public ResponseEntity<PointChargeResponse> savePointChargeRequest(@MemberId Long memberId,
                                                                      @RequestBody PointRequest request) {

        return ResponseEntity.ok(pointService.savePointChargeRequest(memberId, request.price()));
    }

    @Operation(summary = "포인트 출금 요청", description = "시니또가 포인트 출금을 요청합니다.")
    @PostMapping("/withdraw")
    public ResponseEntity<Void> savePointWithdrawRequest(@MemberId Long memberId,
                                                         @RequestBody PointRequest request) {

        pointService.savePointWithdrawRequest(memberId, request.price());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "포인트 로그 조회", description = "포인트 로그를 조회합니다.")
    @GetMapping("/logs")
    public ResponseEntity<Page<PointLogResponse>> getPointLogs(@MemberId Long memberId,
                                                               @PageableDefault(sort = "postTime", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(pointService.getPointLogs(memberId, pageable));
    }

}
