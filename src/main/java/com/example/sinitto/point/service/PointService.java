package com.example.sinitto.point.service;

import com.example.sinitto.common.exception.BadRequestException;
import com.example.sinitto.common.exception.ConflictException;
import com.example.sinitto.common.exception.ForbiddenException;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.common.service.KakaoMessageService;
import com.example.sinitto.common.service.SlackMessageService;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.dto.PointChargeResponse;
import com.example.sinitto.point.dto.PointLogResponse;
import com.example.sinitto.point.dto.PointResponse;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import com.example.sinitto.sinitto.entity.SinittoBankInfo;
import com.example.sinitto.sinitto.repository.SinittoBankInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PointService {

    private static final int MINIMUM_WITHDRAW_POINT = 5000;

    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;
    private final SinittoBankInfoRepository sinittoBankInfoRepository;
    private final KakaoMessageService kakaoMessageService;
    private final SlackMessageService slackMessageService;

    public PointService(MemberRepository memberRepository, PointRepository pointRepository, PointLogRepository pointLogRepository, SinittoBankInfoRepository sinittoBankInfoRepository, KakaoMessageService kakaoMessageService, SlackMessageService slackMessageService) {
        this.memberRepository = memberRepository;
        this.pointRepository = pointRepository;
        this.pointLogRepository = pointLogRepository;
        this.sinittoBankInfoRepository = sinittoBankInfoRepository;
        this.kakaoMessageService = kakaoMessageService;
        this.slackMessageService = slackMessageService;
    }

    @Transactional(readOnly = true)
    public PointResponse getPoint(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("요청한 멤버를 찾을 수 없습니다"));

        Point point = pointRepository.findByMember(member)
                .orElseThrow(() -> new NotFoundException("요청한 멤버의 포인트를 찾을 수 없습니다"));

        return new PointResponse(point.getPrice());
    }

    @Transactional(readOnly = true)
    public Page<PointLogResponse> getPointLogs(Long memberId, Pageable pageable) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("요청한 멤버를 찾을 수 없습니다"));

        return pointLogRepository.findAllByMember(member, pageable)
                .map(pointLog -> new PointLogResponse(
                        pointLog.getPostTime(),
                        pointLog.getContent().getMessage(),
                        pointLog.getPrice(),
                        pointLog.getStatus()
                ));
    }

    @Transactional
    public PointChargeResponse savePointChargeRequest(Long memberId, int price) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("요청한 멤버를 찾을 수 없습니다"));

        if (pointLogRepository.existsByMemberAndStatusIn(member, List.of(PointLog.Status.CHARGE_REQUEST, PointLog.Status.CHARGE_WAITING))) {
            throw new ConflictException("이미 진행중인 포인트 충전 요청이 존재합니다.");
        }

        pointLogRepository.save(new PointLog(PointLog.Content.CHARGE_REQUEST, member, price, PointLog.Status.CHARGE_REQUEST));

        kakaoMessageService.sendPointChargeRequestReceivedMessage(member.getEmail(), price, member.getName(), member.getDepositMessage());

        String title = "포인트 충전 요청";
        String description = String.format("%s님이 %d 포인트를 충전 요청했습니다.", member.getName(), price);
        slackMessageService.sendStyledSlackMessage(title, description, "충전");

        return new PointChargeResponse(member.getDepositMessage());
    }

    @Transactional
    public void savePointWithdrawRequest(Long memberId, int price) {

        if (price < MINIMUM_WITHDRAW_POINT) {
            throw new BadRequestException(String.format("출금시 최소 %d 이상 요청하셔야합니다.", MINIMUM_WITHDRAW_POINT));
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("요청한 멤버를 찾을 수 없습니다"));

        if (!member.isSinitto()) {
            throw new ForbiddenException("출금 요청은 시니또만 가능합니다. 지금 요청은 시니또가 요청하지 않았습니다.");
        }

        if (pointLogRepository.existsByMemberAndStatusIn(member, List.of(PointLog.Status.WITHDRAW_REQUEST, PointLog.Status.WITHDRAW_WAITING))) {
            throw new ConflictException("이미 진행중인 포인트 출금 요청이 존재합니다.");
        }

        if (!sinittoBankInfoRepository.existsByMemberId(memberId)) {
            throw new NotFoundException("시니또의 은행 계좌 정보가 없습니다. 계좌를 등록해야 합니다.");
        }

        Point point = pointRepository.findByMember(member)
                .orElseThrow(() -> new NotFoundException("요청한 멤버의 포인트를 찾을 수 없습니다"));

        if (!point.isSufficientForDeduction(price)) {
            throw new BadRequestException(String.format("보유한 포인트(%d) 보다 더 많은 포인트에 대한 출금요청입니다", point.getPrice()));
        }

        point.deduct(price);

        pointLogRepository.save(new PointLog(PointLog.Content.WITHDRAW_REQUEST, member, price, PointLog.Status.WITHDRAW_REQUEST));

        SinittoBankInfo sinittoBankInfo = sinittoBankInfoRepository.findByMemberId(memberId).orElseThrow(() -> new NotFoundException("시니또의 은행 계좌 정보가 없습니다."));
        kakaoMessageService.sendPointWithdrawRequestReceivedMessage(member.getEmail(), price, member.getName(), sinittoBankInfo.getBankName(), sinittoBankInfo.getAccountNumber());

        String title = "포인트 출금 요청";
        String description = String.format("%s님이 %d 포인트를 출금 요청했습니다.\n은행: %s, 계좌번호: %s",
                member.getName(), price, sinittoBankInfo.getBankName(), sinittoBankInfo.getAccountNumber());
        slackMessageService.sendStyledSlackMessage(title, description, "출금");
    }

    @Transactional
    public void earnPoint(Long memberId, int price, PointLog.Content contentForPointLog) {

        Point point = pointRepository.findByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException("멤버에 연관된 포인트가 없습니다."));

        point.earn(price);

        pointLogRepository.save(
                new PointLog(
                        contentForPointLog,
                        point.getMember(),
                        price,
                        PointLog.Status.EARN)
        );
    }

    @Transactional
    public void deductPoint(Long memberId, int price, PointLog.Content contentForPointLog) {

        Point point = pointRepository.findByMemberIdWithWriteLock(memberId)
                .orElseThrow(() -> new NotFoundException("멤버에 연관된 포인트가 없습니다."));

        if (!point.isSufficientForDeduction(price)) {
            throw new BadRequestException("포인트가 부족합니다.");
        }

        point.deduct(price);

        pointLogRepository.save(
                new PointLog(
                        contentForPointLog,
                        point.getMember(),
                        price,
                        PointLog.Status.SPEND_COMPLETE
                ));
    }

    @Transactional
    public void refundPointByDelete(Long memberId, int price, PointLog.Content contentForPointLog) {

        Point point = pointRepository.findByMemberIdWithWriteLock(memberId)
                .orElseThrow(() -> new NotFoundException("멤버에 연관된 포인트가 없습니다."));

        if (!point.isSufficientForDeduction(price)) {
            throw new BadRequestException("포인트가 부족합니다.");
        }

        point.earn(price);

        pointLogRepository.save(
                new PointLog(
                        contentForPointLog,
                        point.getMember(),
                        price,
                        PointLog.Status.SPEND_CANCEL
                ));
    }

}
