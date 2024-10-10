package com.example.sinitto.point.service;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.dto.PointLogResponse;
import com.example.sinitto.point.dto.PointResponse;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.exception.PointNotFoundException;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;

    public PointService(MemberRepository memberRepository, PointRepository pointRepository, PointLogRepository pointLogRepository) {
        this.memberRepository = memberRepository;
        this.pointRepository = pointRepository;
        this.pointLogRepository = pointLogRepository;
    }

    public PointResponse getPoint(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("요청한 멤버를 찾을 수 없습니다"));

        Point point = pointRepository.findByMember(member)
                .orElseThrow(() -> new PointNotFoundException("요청한 멤버의 포인트를 찾을 수 없습니다"));

        return new PointResponse(point.getPrice());
    }

    public Page<PointLogResponse> getPointLogs(Long memberId, Pageable pageable) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("요청한 멤버를 찾을 수 없습니다"));

        return pointLogRepository.findAllByMember(member, pageable)
                .map(pointLog -> new PointLogResponse(
                        pointLog.getPostTime(),
                        pointLog.getContent(),
                        pointLog.getPrice(),
                        pointLog.getStatus()
                ));
    }

    public void savePointChargeRequest(Long memberId, int price) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("멤버 없음"));

        pointLogRepository.save(new PointLog("포인트 충전 요청", member, price, PointLog.Status.CHARGE_REQUEST));
    }
}
