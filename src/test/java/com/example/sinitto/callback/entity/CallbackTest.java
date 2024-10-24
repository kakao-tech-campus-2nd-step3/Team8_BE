package com.example.sinitto.callback.entity;

import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.common.exception.ConflictException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CallbackTest {

    @Autowired
    private CallbackRepository callbackRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SeniorRepository seniorRepository;
    private Callback testCallback;
    private Member testMember;
    private Senior testSenior;

    @BeforeEach
    void setUp() {
        testMember = memberRepository.save(new Member("member", "01043214321", "tjdgns5506@gmai.com", true));

        testSenior = seniorRepository.save(new Senior("senior", "01012341234", testMember));

        testCallback = callbackRepository.save(new Callback(Callback.Status.WAITING, testSenior));
    }

    @Test
    @DisplayName("Waiting -> In Progress - 성공")
    void changeStatusToInProgress() {
        //when
        testCallback.changeStatusToInProgress();

        //then
        assertEquals(testCallback.getStatus(), Callback.Status.IN_PROGRESS.name());
    }

    @Test
    @DisplayName("Pending Complete -> In Progress - 예외 발생")
    void changeStatusToInProgress_fail1() {
        //given
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToPendingComplete();

        //when then
        assertThrows(ConflictException.class, () -> testCallback.changeStatusToInProgress());
    }

    @Test
    @DisplayName("In Progress -> In Progress - 예외 발생")
    void changeStatusToInProgress_fail2() {
        //given
        testCallback.changeStatusToInProgress();

        //when then
        assertThrows(ConflictException.class, () -> testCallback.changeStatusToInProgress());
    }

    @Test
    @DisplayName("In Progress -> Pending Complete - 성공")
    void changeStatusToPendingComplete() {
        //given
        testCallback.changeStatusToInProgress();

        //when
        testCallback.changeStatusToPendingComplete();

        //then
        assertEquals(testCallback.getStatus(), Callback.Status.PENDING_COMPLETE.name());
    }

    @Test
    @DisplayName("Waiting -> Pending Complete - 예외 발생")
    void changeStatusToPendingComplete_fail1() {
        //when then
        assertThrows(ConflictException.class, () -> testCallback.changeStatusToPendingComplete());
    }

    @Test
    @DisplayName("Pending Complete -> Pending Complete - 예외 발생")
    void changeStatusToPendingComplete_fail2() {
        //given
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToPendingComplete();

        //when then
        assertThrows(ConflictException.class, () -> testCallback.changeStatusToPendingComplete());
    }

    @Test
    @DisplayName("In Progress -> Waiting - 성공")
    void changeStatusToWaiting() {
        //given
        testCallback.changeStatusToInProgress();

        //when
        testCallback.changeStatusToWaiting();

        //then
        assertEquals(testCallback.getStatus(), Callback.Status.WAITING.name());
    }

    @Test
    @DisplayName("Waiting -> Waiting - 예외 발생")
    void changeStatusToWaiting_fain1() {
        //when then
        assertThrows(ConflictException.class, () -> testCallback.changeStatusToWaiting());
    }

    @Test
    @DisplayName("Pending Complete -> Waiting - 예외 발생")
    void changeStatusToWaiting_fail2() {
        //given
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToPendingComplete();

        //when then
        assertThrows(ConflictException.class, () -> testCallback.changeStatusToWaiting());
    }

    @Test
    @DisplayName("Waiting 상태에서 멤버Id 할당 - 성공")
    void checkAssignedMemberId() {
        //given
        testCallback.assignMember(3L);

        //when
        Long assignedMemberId = testCallback.getAssignedMemberId();

        //then
        assertEquals(assignedMemberId, 3L);
    }

    @Test
    @DisplayName("In Progress 상태에서 멤버Id 할당 - 예외 발생")
    void checkAssignedMemberId_fail1() {
        //given
        testCallback.changeStatusToInProgress();

        //when then
        assertThrows(ConflictException.class, () -> testCallback.assignMember(3L));
    }

    @Test
    @DisplayName("Pending Complete 상태에서 멤버Id 할당 - 예외 발생")
    void checkAssignedMemberId_fail2() {
        //given
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToPendingComplete();

        //when then
        assertThrows(ConflictException.class, () -> testCallback.assignMember(3L));
    }

    @Test
    @DisplayName("Complete 상태에서 멤버Id 할당 - 예외 발생")
    void checkAssignedMemberId_fail3() {
        //given
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToPendingComplete();
        testCallback.changeStatusToComplete();

        //when then
        assertThrows(ConflictException.class, () -> testCallback.assignMember(3L));
    }

    @Test
    @DisplayName("InProgress 상태에서 할당 취소 - 성공")
    void cancelAssignment() {
        //given
        testCallback.assignMember(1L);
        testCallback.changeStatusToInProgress();
        assertEquals(1L, testCallback.getAssignedMemberId());

        //when
        testCallback.cancelAssignment();

        //then
        assertNull(testCallback.getAssignedMemberId());
    }

    @Test
    @DisplayName("Pending Complete 상태에서 할당 취소 - 예외 발생")
    void cancelAssignment_fail1() {
        //given
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToPendingComplete();

        //when then
        assertThrows(ConflictException.class, () -> testCallback.cancelAssignment());
    }

    @Test
    @DisplayName("Waiting 상태에서 할당 취소 - 예외 발생")
    void cancelAssignment_fail2() {
        //when then
        assertThrows(ConflictException.class, () -> testCallback.cancelAssignment());
    }

    @Test
    @DisplayName("Complete 상태에서 할당 취소 - 예외 발생")
    void cancelAssignment_fail3() {
        //given
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToPendingComplete();
        testCallback.changeStatusToComplete();

        //when then
        assertThrows(ConflictException.class, () -> testCallback.cancelAssignment());
    }

    @Test
    @DisplayName("Pending Complete -> Complete - 성공")
    void changeStatusToComplete() {
        //when
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToPendingComplete();
        testCallback.changeStatusToComplete();

        //then
        assertEquals(testCallback.getStatus(), Callback.Status.COMPLETE.name());
    }

    @Test
    @DisplayName("Complete -> Complete - 예외 발생")
    void changeStatusToComplete_fail() {
        //when
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToPendingComplete();
        testCallback.changeStatusToComplete();

        //then
        assertThrows(ConflictException.class, () -> testCallback.changeStatusToComplete());
    }

    @Test
    @DisplayName("Waiting -> Complete - 예외 발생")
    void changeStatusToComplete_fail1() {
        //when then
        assertThrows(ConflictException.class, () -> testCallback.changeStatusToComplete());
    }

    @Test
    @DisplayName("In Progress -> Complete - 예외 발생")
    void changeStatusToComplete_fail2() {
        //given
        testCallback.changeStatusToInProgress();

        //when then
        assertThrows(ConflictException.class, () -> testCallback.changeStatusToComplete());
    }

    @Test
    @DisplayName("보호자에 연관된 다수의 시니어 리스트를 얻은후, 다수의 시니어들이 신청한 콜백 요청 기록 조회 테스트(CallbackService 의 getCallbackHistoryOfGuard() 테스트나 마찬가지)")
    void getSeniorListAndReadCallbackHistoryOfSeniors() {
        //given
        Senior testSenior2 = new Senior("senior2", "01099999999", testMember);
        seniorRepository.save(testSenior2);

        callbackRepository.save(new Callback(Callback.Status.WAITING, testSenior2));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, testSenior2));
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "postTime"));
        //when
        Page<Callback> result = callbackRepository.findAllBySeniorIn(List.of(testSenior, testSenior2), pageable);

        //then
        assertEquals(3, result.getTotalPages());
        assertEquals(testSenior2, result.getContent().getFirst().getSenior());
        assertEquals(Callback.Status.COMPLETE.toString(), result.getContent().getFirst().getStatus());
    }

}
