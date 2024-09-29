package com.example.sinitto.callback.entity;

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

        testCallback = callbackRepository.save(new Callback(CallbackStatus.WAITING, testSenior, 1L));
    }

    @Test
    void changeStatusToInProgress() {
        //when
        testCallback.changeStatusToInProgress();

        //then
        assertEquals(testCallback.getStatus(), CallbackStatus.IN_PROGRESS.name());
    }

    @Test
    void changeStatusToComplete() {
        //when
        testCallback.changeStatusToComplete();

        //then
        assertEquals(testCallback.getStatus(), CallbackStatus.COMPLETE.name());
    }

    @Test
    void checkAssignedMemberId() {
        //when
        Long assignedMemberId = testCallback.getAssignedMemberId();

        //then
        assertEquals(assignedMemberId, 1L);
    }

    @Test
    void cancelAssignment() {
        //given
        testCallback.cancelAssignment();

        //when
        Long assignedMemberId = testCallback.getAssignedMemberId();

        //then
        assertEquals(assignedMemberId, 0L);
    }

}
