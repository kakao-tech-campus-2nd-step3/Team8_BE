package com.example.sinitto.point.service;

import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.exception.PointLogNotFoundException;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PointAdminService {

    private final PointLogRepository pointLogRepository;
    private final PointRepository pointRepository;

    public PointAdminService(PointLogRepository pointLogRepository, PointRepository pointRepository) {
        this.pointLogRepository = pointLogRepository;
        this.pointRepository = pointRepository;
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

    @Transactional(readOnly = true)
    public List<PointLog> readAllPointWithdrawRequest() {

        return pointLogRepository.findAllByStatusInOrderByPostTimeDesc(List.of(PointLog.Status.WITHDRAW_REQUEST, PointLog.Status.WITHDRAW_WAITING, PointLog.Status.WITHDRAW_COMPLETE, PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT));
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
}
