package com.example.sinitto.point.service;

import com.example.sinitto.callback.exception.NotSinittoException;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.exception.MemberNotFoundException;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.dto.PointChargeResponse;
import com.example.sinitto.point.dto.PointLogResponse;
import com.example.sinitto.point.dto.PointResponse;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.exception.NotEnoughPointException;
import com.example.sinitto.point.exception.PointNotFoundException;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import com.example.sinitto.sinitto.exception.SinittoBankInfoNotFoundException;
import com.example.sinitto.sinitto.repository.SinittoBankInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {

    public static final double WITHDRAWAL_FEE_RATE = 0.8;
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;
    private final SinittoBankInfoRepository sinittoBankInfoRepository;

    public PointService(MemberRepository memberRepository, PointRepository pointRepository, PointLogRepository pointLogRepository, SinittoBankInfoRepository sinittoBankInfoRepository) {
        this.memberRepository = memberRepository;
        this.pointRepository = pointRepository;
        this.pointLogRepository = pointLogRepository;
        this.sinittoBankInfoRepository = sinittoBankInfoRepository;
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

    @Transactional
    public PointChargeResponse savePointChargeRequest(Long memberId, int price) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("요청한 멤버를 찾을 수 없습니다"));

        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST.getMessage(), member, price, PointLog.Status.CHARGE_REQUEST));

        return new PointChargeResponse(member.getDepositMessage());
    }

    @Transactional
    public void savePointWithdrawRequest(Long memberId, int price) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("요청한 멤버를 찾을 수 없습니다"));

        if (!member.isSinitto()) {
            throw new NotSinittoException("출금 요청은 시니또만 가능합니다. 지금 요청은 시니또가 요청하지 않았습니다.");
        }

        if (!sinittoBankInfoRepository.existsByMemberId(memberId)) {
            throw new SinittoBankInfoNotFoundException("시니또의 은행 계좌 정보가 없습니다. 계좌를 등록해야 합니다.");
        }

        Point point = pointRepository.findByMember(member)
                .orElseThrow(() -> new PointNotFoundException("요청한 멤버의 포인트를 찾을 수 없습니다"));

        int adjustedPrice = (int) (price * WITHDRAWAL_FEE_RATE);

        if (!point.isSufficientForDeduction(price)) {
            throw new NotEnoughPointException(String.format("보유한 포인트(%d) 보다 더 많은 포인트에 대한 출금요청입니다", point.getPrice()));
        }

        point.deduct(price);

        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST.getMessage(), member, adjustedPrice, PointLog.Status.WITHDRAW_REQUEST));
    }
}
