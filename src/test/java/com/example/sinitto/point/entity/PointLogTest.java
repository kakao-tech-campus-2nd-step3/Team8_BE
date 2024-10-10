package com.example.sinitto.point.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.exception.InvalidPointStatusException;
import com.example.sinitto.point.repository.PointLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class PointLogTest {

    @Autowired
    PointLogRepository pointLogRepository;
    @Autowired
    MemberRepository memberRepository;

    Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member("James", "01099991234", "g@gmail.com", true));
    }

    @Test
    @DisplayName("포인트 로그 ChargeWaiting 상태로 전환 성공")
    void changeStatusToChargeWaiting() {
        PointLog pointLog = pointLogRepository.save(new PointLog("content", member, 1000, PointLog.Status.CHARGE_REQUEST));

        pointLog.changeStatusToChargeWaiting();

        assertEquals(PointLog.Status.CHARGE_WAITING, pointLog.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EARN", "SPEND_RESERVE", "SPEND_COMPLETE", "SPEND_CANCEL", "REFUND", "WITHDRAW_REQUEST", "WITHDRAW_WAITING", "WITHDRAW_COMPLETE", "CHARGE_WAITING", "CHARGE_COMPLETE"})
    @DisplayName("포인트 로그 ChargeWaiting 상태로 전환 실패")
    void changeStatusToChargeWaiting_fail(String initialStatus) {
        PointLog.Status status = PointLog.Status.valueOf(initialStatus);
        PointLog pointLog = pointLogRepository.save(new PointLog("content", member, 1000, status));

        assertThrows(InvalidPointStatusException.class, pointLog::changeStatusToChargeWaiting);
    }

    @Test
    @DisplayName("포인트 로그 ChargeComplete 상태로 전환 성공")
    void changeStatusToChargeComplete() {
        PointLog pointLog = pointLogRepository.save(new PointLog("content", member, 1000, PointLog.Status.CHARGE_WAITING));

        pointLog.changeStatusToChargeComplete();

        assertEquals(PointLog.Status.CHARGE_COMPLETE, pointLog.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EARN", "SPEND_RESERVE", "SPEND_COMPLETE", "SPEND_CANCEL", "REFUND", "WITHDRAW_REQUEST", "WITHDRAW_WAITING", "WITHDRAW_COMPLETE", "CHARGE_REQUEST", "CHARGE_COMPLETE"})
    @DisplayName("포인트 로그 ChargeComplete 상태로 전환 실패")
    void changeStatusToChargeComplete_fail(String initialStatus) {
        PointLog.Status status = PointLog.Status.valueOf(initialStatus);
        PointLog pointLog = pointLogRepository.save(new PointLog("content", member, 1000, status));

        assertThrows(InvalidPointStatusException.class, pointLog::changeStatusToChargeComplete);
    }
}