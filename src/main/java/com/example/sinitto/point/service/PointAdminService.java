package com.example.sinitto.point.service;

import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.common.service.KakaoMessageService;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.dto.PointLogWithBankInfo;
import com.example.sinitto.point.dto.PointLogWithDepositMessage;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import com.example.sinitto.sinitto.entity.SinittoBankInfo;
import com.example.sinitto.sinitto.repository.SinittoBankInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PointAdminService {

    private final PointLogRepository pointLogRepository;
    private final PointRepository pointRepository;
    private final SinittoBankInfoRepository sinittoBankInfoRepository;
    private final MemberRepository memberRepository;
    private final KakaoMessageService kakaoMessageService;

    public PointAdminService(PointLogRepository pointLogRepository, PointRepository pointRepository, SinittoBankInfoRepository sinittoBankInfoRepository, MemberRepository memberRepository, KakaoMessageService kakaoMessageService) {
        this.pointLogRepository = pointLogRepository;
        this.pointRepository = pointRepository;
        this.sinittoBankInfoRepository = sinittoBankInfoRepository;
        this.memberRepository = memberRepository;
        this.kakaoMessageService = kakaoMessageService;
    }

    @Transactional(readOnly = true)
    public List<PointLogWithDepositMessage> getPointLogWithDepositMessage() {

        List<PointLog> pointLogs = pointLogRepository.findAllByStatusInOrderByPostTimeDesc(List.of(PointLog.Status.CHARGE_WAITING, PointLog.Status.CHARGE_REQUEST, PointLog.Status.CHARGE_COMPLETE, PointLog.Status.CHARGE_FAIL));
        List<PointLogWithDepositMessage> logWithDepositMessages = new ArrayList<>();

        for (PointLog pointLog : pointLogs) {
            Member member = memberRepository.findById(pointLog.getMember().getId())
                    .orElse(new Member("미등록 유저", "미등록 유저", "미등록 유저", false));

            PointLogWithDepositMessage pointLogWithDepositMessage = new PointLogWithDepositMessage(
                    pointLog.getId(),
                    pointLog.getPrice(),
                    pointLog.getPostTime().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
                    pointLog.getStatus(),
                    member.getDepositMessage()
            );

            logWithDepositMessages.add(pointLogWithDepositMessage);
        }
        return logWithDepositMessages;
    }

    @Transactional
    public void changeChargeLogToWaiting(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new NotFoundException("포인트 로그를 찾을 수 없습니다"));

        pointLog.changeStatusToChargeWaiting();
    }

    @Transactional
    public void earnPointAndChangeToChargeComplete(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new NotFoundException("포인트 로그를 찾을 수 없습니다."));

        Point point = pointRepository.findByMember(pointLog.getMember())
                .orElseThrow(() -> new NotFoundException("포인트를 찾을 수 없습니다."));

        pointLog.changeStatusToChargeComplete();
        point.earn(pointLog.getPrice());

        kakaoMessageService.sendPointChargeCompleteMessage(pointLog.getMember().getEmail(), pointLog.getPrice(), pointLog.getMember().getName());
    }

    @Transactional
    public void changeChargeLogToFail(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new NotFoundException("포인트 로그를 찾을 수 없습니다."));

        pointLog.changeStatusToChargeFail();
    }

    @Transactional
    public void changeWithdrawLogToWaiting(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new NotFoundException("포인트 로그를 찾을 수 없습니다."));

        pointLog.changeStatusToWithdrawWaiting();
    }

    @Transactional
    public void changeWithdrawLogToComplete(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new NotFoundException("포인트 로그를 찾을 수 없습니다."));

        pointLog.changeStatusToWithdrawComplete();

        SinittoBankInfo sinittoBankInfo = sinittoBankInfoRepository.findByMemberId(pointLog.getMember().getId()).orElseThrow(() -> new NotFoundException("시니또의 은행 계좌 정보가 없습니다."));
        kakaoMessageService.sendPointWithdrawCompleteMessage(pointLog.getMember().getEmail(), pointLog.getPrice(), pointLog.getMember().getName(), sinittoBankInfo.getBankName(), sinittoBankInfo.getAccountNumber());
    }

    @Transactional
    public void changeWithdrawLogToFail(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new NotFoundException("포인트 로그를 찾을 수 없습니다."));

        Point point = pointRepository.findByMember(pointLog.getMember())
                .orElseThrow(() -> new NotFoundException("포인트를 찾을 수 없습니다."));

        point.earn(pointLog.getPrice());

        pointLog.changeStatusToWithdrawFail();
    }

    @Transactional(readOnly = true)
    public List<PointLogWithBankInfo> getPointLogWithBankInfo() {

        List<PointLog> withdrawPointLogs = pointLogRepository.findAllByStatusInOrderByPostTimeDesc(List.of(PointLog.Status.WITHDRAW_REQUEST, PointLog.Status.WITHDRAW_WAITING, PointLog.Status.WITHDRAW_COMPLETE, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));

        List<PointLogWithBankInfo> logWithBankInfos = new ArrayList<>();

        for (PointLog pointLog : withdrawPointLogs) {
            SinittoBankInfo sinittoBankInfo = sinittoBankInfoRepository.findByMemberId(pointLog.getMember().getId())
                    .orElse(new SinittoBankInfo("등록된 계좌 없음", "등록된 계좌 없음", null));

            PointLogWithBankInfo pointLogWithBankInfo = new PointLogWithBankInfo(
                    pointLog.getId(),
                    pointLog.getPointPriceAfterFee(),
                    pointLog.getPostTime().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분")),
                    pointLog.getStatus(),
                    sinittoBankInfo.getBankName(),
                    sinittoBankInfo.getAccountNumber()
            );
            logWithBankInfos.add(pointLogWithBankInfo);
        }
        return logWithBankInfos;
    }
}
