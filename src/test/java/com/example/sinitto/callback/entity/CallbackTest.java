package com.example.sinitto.callback.entity;

import com.example.sinitto.callback.exception.AlreadyCompleteException;
import com.example.sinitto.callback.exception.AlreadyInProgressException;
import com.example.sinitto.callback.exception.AlreadyWaitingException;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.member.repository.SeniorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class CallbackTest {

    @Autowired
    private CallbackRepository callbackRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SeniorRepository seniorRepository;
    private Callback testCallback;

    @BeforeEach
    void setUp() {
        Member testMember = new Member("member", "01043214321", "tjdgns5506@gmai.com", true);
        memberRepository.save(testMember);

        Senior testSenior = new Senior("senior", "01012341234", testMember);
        seniorRepository.save(testSenior);

        testCallback = callbackRepository.save(new Callback(Callback.Status.WAITING, testSenior));
    }

    @Test
    void changeStatusToInProgress() {
        //when
        testCallback.changeStatusToInProgress();

        //then
        assertEquals(testCallback.getStatus(), Callback.Status.IN_PROGRESS.name());
    }

    @Test
    void changeStatusToInProgress_fail1() {
        //"진행"으로 바꿀 수 있는 상태는 "대기" 상태 뿐이다.
        //given
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToComplete();

        //when then
        assertThrows(AlreadyCompleteException.class, () -> testCallback.changeStatusToInProgress());
    }

    @Test
    void changeStatusToInProgress_fail2() {
        //"진행"으로 바꿀 수 있는 상태는 "대기" 상태 뿐이다.
        //given
        testCallback.changeStatusToInProgress();

        //when then
        assertThrows(AlreadyInProgressException.class, () -> testCallback.changeStatusToInProgress());
    }

    @Test
    void changeStatusToComplete() {
        //"완료"으로 바꿀 수 있는 상태는 "진행" 상태 뿐이다.
        //given
        testCallback.changeStatusToInProgress();

        //when
        testCallback.changeStatusToComplete();

        //then
        assertEquals(testCallback.getStatus(), Callback.Status.COMPLETE.name());
    }

    @Test
    void changeStatusToComplete_fail1() {
        //"완료"으로 바꿀 수 있는 상태는 "진행" 상태 뿐이다.
        //given
        //testCallback.changeStatusToWaiting(); 이미 대기
        //when then
        assertThrows(AlreadyWaitingException.class, () -> testCallback.changeStatusToComplete());    }

    @Test
    void changeStatusToComplete_fail2() {
        //"완료"으로 바꿀 수 있는 상태는 "진행" 상태 뿐이다.
        //given
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToComplete();

        //when then
        assertThrows(AlreadyCompleteException.class, () -> testCallback.changeStatusToComplete());
    }



    @Test
    void checkAssignedMemberId() {
        //given
        testCallback.assignMember(3L);

        //when
        Long assignedMemberId = testCallback.getAssignedMemberId();

        //then
        assertEquals(assignedMemberId, 3L);
    }

    @Test
    void cancelAssignment() {
        //given
        testCallback.assignMember(23L);
        testCallback.changeStatusToInProgress();

        //when
        testCallback.cancelAssignment();
        Long assignedMemberId = testCallback.getAssignedMemberId();

        //then
        assertEquals(assignedMemberId, 0L);
    }

    @Test
    void assignMember() {
        //when
        testCallback.assignMember(101L);

        //then
        assertEquals(101L, testCallback.getAssignedMemberId());
    }
}
