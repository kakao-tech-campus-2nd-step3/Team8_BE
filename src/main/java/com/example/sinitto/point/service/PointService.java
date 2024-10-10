package com.example.sinitto.point.service;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.dto.PointResponse;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.exception.PointNotFoundException;
import com.example.sinitto.point.repository.PointRepository;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    public PointService(MemberRepository memberRepository, PointRepository pointRepository) {
        this.memberRepository = memberRepository;
        this.pointRepository = pointRepository;
    }

    public PointResponse getPoint(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("요청한 멤버를 찾을 수 없습니다"));

        Point point = pointRepository.findByMember(member)
                .orElseThrow(()->new PointNotFoundException("요청한 멤버의 포인트를 찾을 수 없습니다"));

        return new PointResponse(point.getPrice());
    }

}
