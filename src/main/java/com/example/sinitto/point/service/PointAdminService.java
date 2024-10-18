package com.example.sinitto.point.service;

import com.example.sinitto.sinitto.entity.SinittoBankInfo;
import com.example.sinitto.point.dto.PointLogWithBankInfo;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.exception.PointLogNotFoundException;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import com.example.sinitto.sinitto.repository.SinittoBankInfoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointAdminService {

    private final PointLogRepository pointLogRepository;
    private final PointRepository pointRepository;
    private final SinittoBankInfoRepository sinittoBankInfoRepository;

    public PointAdminService(PointLogRepository pointLogRepository, PointRepository pointRepository, SinittoBankInfoRepository sinittoBankInfoRepository) {
        this.pointLogRepository = pointLogRepository;
        this.pointRepository = pointRepository;
        this.sinittoBankInfoRepository = sinittoBankInfoRepository;
    }

    public List<PointLog> readAllNotCompletedPointChargeRequest() {

        return pointLogRepository.findAllByStatusInOrderByPostTimeDesc(List.of(PointLog.Status.CHARGE_WAITING, PointLog.Status.CHARGE_REQUEST, PointLog.Status.CHARGE_COMPLETE, PointLog.Status.CHARGE_FAIL));
    }

    @Transactional
    public void changeChargeLogToWaiting(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new PointLogNotFoundException("포인트 로그를 찾을 수 없습니다"));

        pointLog.changeStatusToChargeWaiting();
    }

    @Transactional
    public void earnPointAndChangeToChargeComplete(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new PointLogNotFoundException("포인트 로그를 찾을 수 없습니다."));

        Point point = pointRepository.findByMember(pointLog.getMember())
                .orElseThrow(() -> new PointLogNotFoundException("포인트를 찾을 수 없습니다."));

        pointLog.changeStatusToChargeComplete();
        point.earn(pointLog.getPrice());
    }

    @Transactional
    public void changeChargeLogToFail(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new PointLogNotFoundException("포인트 로그를 찾을 수 없습니다."));

        pointLog.changeStatusToChargeFail();
    }

    @Transactional
    public void changeWithdrawLogToWaiting(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new PointLogNotFoundException("포인트 로그를 찾을 수 없습니다."));

        pointLog.changeStatusToWithdrawWaiting();
    }

    @Transactional
    public void changeWithdrawLogToComplete(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new PointLogNotFoundException("포인트 로그를 찾을 수 없습니다."));

        pointLog.changeStatusToWithdrawComplete();
    }

    @Transactional
    public void changeWithdrawLogToFail(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new PointLogNotFoundException("포인트 로그를 찾을 수 없습니다."));

        Point point = pointRepository.findByMember(pointLog.getMember())
                .orElseThrow(() -> new PointLogNotFoundException("포인트를 찾을 수 없습니다."));

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
                    pointLog.getPrice(),
                    pointLog.getPostTime(),
                    pointLog.getStatus(),
                    sinittoBankInfo.getBankName(),
                    sinittoBankInfo.getAccountNumber()
            );
            logWithBankInfos.add(pointLogWithBankInfo);
        }
        return logWithBankInfos;
    }
}
