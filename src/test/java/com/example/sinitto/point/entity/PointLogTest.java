package com.example.sinitto.point.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.point.exception.InvalidPointLogStatusException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class PointLogTest {

    Member member = new Member("test", "test", "test", true);

    @Test
    @DisplayName("포인트 로그 ChargeWaiting 상태로 전환 성공")
    void changeStatusToChargeWaiting() {
        //given
        PointLog pointLog = new PointLog("content", member, 1000, PointLog.Status.CHARGE_REQUEST);

        //when
        pointLog.changeStatusToChargeWaiting();

        //then
        assertEquals(PointLog.Status.CHARGE_WAITING, pointLog.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EARN", "SPEND_COMPLETE", "SPEND_CANCEL", "WITHDRAW_REQUEST", "WITHDRAW_WAITING", "WITHDRAW_COMPLETE", "CHARGE_WAITING", "CHARGE_COMPLETE"})
    @DisplayName("포인트 로그 ChargeWaiting 상태로 전환 실패")
    void changeStatusToChargeWaiting_fail(String initialStatus) {
        //given
        PointLog.Status status = PointLog.Status.valueOf(initialStatus);
        PointLog pointLog = new PointLog("content", member, 1000, status);

        //when then
        assertThrows(InvalidPointLogStatusException.class, pointLog::changeStatusToChargeWaiting);
    }

    @Test
    @DisplayName("포인트 로그 ChargeComplete 상태로 전환 성공")
    void changeStatusToChargeComplete() {
        //given
        PointLog pointLog = new PointLog("content", member, 1000, PointLog.Status.CHARGE_WAITING);

        //when
        pointLog.changeStatusToChargeComplete();

        //then
        assertEquals(PointLog.Status.CHARGE_COMPLETE, pointLog.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EARN", "SPEND_COMPLETE", "SPEND_CANCEL", "WITHDRAW_REQUEST", "WITHDRAW_WAITING", "WITHDRAW_COMPLETE", "CHARGE_REQUEST", "CHARGE_COMPLETE"})
    @DisplayName("포인트 로그 ChargeComplete 상태로 전환 실패")
    void changeStatusToChargeComplete_fail(String initialStatus) {
        //given
        PointLog.Status status = PointLog.Status.valueOf(initialStatus);
        PointLog pointLog = new PointLog("content", member, 1000, status);

        //when then
        assertThrows(InvalidPointLogStatusException.class, pointLog::changeStatusToChargeComplete);
    }

    @Test
    @DisplayName("포인트 로그 ChargeFail 상태로 전환 성공")
    void changeStatusToChargeFail() {
        //given
        PointLog pointLog = new PointLog("content", member, 1000, PointLog.Status.CHARGE_WAITING);

        //when
        pointLog.changeStatusToChargeFail();

        //then
        assertEquals(PointLog.Status.CHARGE_FAIL, pointLog.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EARN", "SPEND_COMPLETE", "SPEND_CANCEL", "WITHDRAW_REQUEST", "WITHDRAW_WAITING", "WITHDRAW_COMPLETE","WITHDRAW_FAIL_AND_RESTORE_POINT", "CHARGE_REQUEST", "CHARGE_COMPLETE","CHARGE_FAIL"})
    @DisplayName("포인트 로그 ChargeFail 상태로 전환 실패")
    void changeStatusToChargeFail_fail(String initialStatus) {
        //given
        PointLog.Status status = PointLog.Status.valueOf(initialStatus);
        PointLog pointLog = new PointLog("content", member, 1000, status);

        //when then
        assertThrows(InvalidPointLogStatusException.class, pointLog::changeStatusToChargeFail);
    }

    @Test
    @DisplayName("포인트 로그 WithdrawWaiting 상태로 전환 성공")
    void changeStatusToWithdrawWaiting() {
        //given
        PointLog pointLog = new PointLog("content", member, 1000, PointLog.Status.WITHDRAW_REQUEST);

        //when
        pointLog.changeStatusToWithdrawWaiting();

        //then
        assertEquals(PointLog.Status.WITHDRAW_WAITING, pointLog.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EARN", "SPEND_COMPLETE", "SPEND_CANCEL", "WITHDRAW_WAITING", "WITHDRAW_COMPLETE", "WITHDRAW_FAIL_AND_RESTORE_POINT", "CHARGE_REQUEST", "CHARGE_COMPLETE", "CHARGE_FAIL","CHARGE_WAITING"})
    @DisplayName("포인트 로그 WithdrawWaiting 상태로 전환 실패")
    void changeStatusToWithdrawWaiting_fail(String initialStatus) {
        //given
        PointLog.Status status = PointLog.Status.valueOf(initialStatus);
        PointLog pointLog = new PointLog("content", member, 1000, status);

        //when then
        assertThrows(InvalidPointLogStatusException.class, pointLog::changeStatusToWithdrawWaiting);
    }

    @Test
    @DisplayName("포인트 로그 WithdrawComplete 상태로 전환 성공")
    void changeStatusToWithdrawComplete() {
        //given
        PointLog pointLog = new PointLog("content", member, 1000, PointLog.Status.WITHDRAW_WAITING);

        //when
        pointLog.changeStatusToWithdrawComplete();

        //then
        assertEquals(PointLog.Status.WITHDRAW_COMPLETE, pointLog.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EARN", "SPEND_COMPLETE", "SPEND_CANCEL", "WITHDRAW_REQUEST", "WITHDRAW_COMPLETE", "WITHDRAW_FAIL_AND_RESTORE_POINT", "CHARGE_REQUEST", "CHARGE_COMPLETE", "CHARGE_FAIL"})
    @DisplayName("포인트 로그 WithdrawComplete 상태로 전환 실패")
    void changeStatusToWithdrawComplete_fail(String initialStatus) {
        //given
        PointLog.Status status = PointLog.Status.valueOf(initialStatus);
        PointLog pointLog = new PointLog("content", member, 1000, status);

        //when then
        assertThrows(InvalidPointLogStatusException.class, pointLog::changeStatusToWithdrawComplete);
    }

    @Test
    @DisplayName("포인트 로그 WithdrawFail 상태로 전환 성공")
    void changeStatusToWithdrawFail() {
        //given
        PointLog pointLog = new PointLog("content", member, 1000, PointLog.Status.WITHDRAW_WAITING);

        //when
        pointLog.changeStatusToWithdrawFail();

        //then
        assertEquals(PointLog.Status.WITHDRAW_FAIL_AND_RESTORE_POINT, pointLog.getStatus());
    }

    @ParameterizedTest
    @ValueSource(strings = {"EARN", "SPEND_COMPLETE", "SPEND_CANCEL", "WITHDRAW_REQUEST", "WITHDRAW_COMPLETE", "WITHDRAW_FAIL_AND_RESTORE_POINT", "CHARGE_REQUEST", "CHARGE_COMPLETE", "CHARGE_FAIL"})
    @DisplayName("포인트 로그 WithdrawFail 상태로 전환 실패")
    void changeStatusToWithdrawFail_fail(String initialStatus) {
        //given
        PointLog.Status status = PointLog.Status.valueOf(initialStatus);
        PointLog pointLog = new PointLog("content", member, 1000, status);

        //when then
        assertThrows(InvalidPointLogStatusException.class, pointLog::changeStatusToWithdrawFail);
    }
}
