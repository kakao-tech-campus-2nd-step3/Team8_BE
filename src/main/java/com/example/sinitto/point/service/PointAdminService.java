package com.example.sinitto.point.service;

import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.exception.PointLogNotFoundException;
import com.example.sinitto.point.repository.PointLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PointAdminService {

    private final PointLogRepository pointLogRepository;

    public PointAdminService(PointLogRepository pointLogRepository) {
        this.pointLogRepository = pointLogRepository;
    }

    public List<PointLog> readAllNotCompletedPointChargeRequest() {

        return pointLogRepository.findAllByStatusIn(List.of(PointLog.Status.CHARGE_WAITING, PointLog.Status.CHARGE_REQUEST));
    }

    @Transactional
    public void changePointLogChargeRequestToChargeWaiting(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new PointLogNotFoundException("포인트 로그를 찾을 수 없습니다"));

        pointLog.changeStatusToChargeWaiting();
    }

    @Transactional
    public void changePointLogChargeWaitingToChargeComplete(Long pointLogId) {

        PointLog pointLog = pointLogRepository.findById(pointLogId)
                .orElseThrow(() -> new PointLogNotFoundException("포인트 로그를 찾을 수 없습니다"));

        pointLog.changeStatusToChargeComplete();
    }
}
