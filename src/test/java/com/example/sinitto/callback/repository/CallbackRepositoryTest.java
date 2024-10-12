package com.example.sinitto.callback.repository;

import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CallbackRepositoryTest {

    @Autowired
    private CallbackRepository callbackRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SeniorRepository seniorRepository;
    private Callback testCallback;
    private Long memberId;

    @BeforeEach
    void setUp() {
        Member testMember = new Member("member", "01043214321", "tjdgns5506@gmai.com", true);
        memberRepository.save(testMember);

        memberId = testMember.getId();

        Senior testSenior = new Senior("senior", "01012341234", testMember);
        seniorRepository.save(testSenior);

        testCallback = callbackRepository.save(new Callback(Callback.Status.WAITING, testSenior));
    }

    @Test
    @DisplayName("할당된 멤버id 조회 테스트 - 성공")
    void findByAssignedMemberId() {
        //given
        testCallback.assignMember(memberId);
        testCallback.changeStatusToInProgress();

        //when
        Callback result = callbackRepository.findByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS).get();

        //then
        assertNotNull(result);
        assertEquals(memberId, result.getAssignedMemberId());
        assertEquals("senior", result.getSeniorName());
    }

    @Test
    @DisplayName("할당된 멤버id 조회 테스트 - 실패(멤버ID 불일치)")
    void findByAssignedMemberId_fail() {
        //given
        testCallback.assignMember(memberId);
        testCallback.changeStatusToInProgress();

        //when
        Optional<Callback> result = callbackRepository.findByAssignedMemberIdAndStatus(100L, Callback.Status.IN_PROGRESS);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("할당된 멤버id 조회 테스트 - 실패(콜백 상태가 InProgress 아닐 경우)")
    void findByAssignedMemberId_fail2() {
        //given
        //현재 Waiting 상태

        //when
        Optional<Callback> result = callbackRepository.findByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("할당된 멤버id 조회 테스트 - 실패(멤버Id 불일치 +콜백 상태가 InProgress 아닐 경우)")
    void findByAssignedMemberId_fail3() {
        //given
        testCallback.assignMember(memberId);
        testCallback.changeStatusToInProgress();
        testCallback.changeStatusToPendingComplete();

        //when
        Optional<Callback> result = callbackRepository.findByAssignedMemberIdAndStatus(100L, Callback.Status.IN_PROGRESS);

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("콜백에 할당된 멤버id와 상태를 매게변수로한 조회 - 성공")
    void existsByAssignedMemberIdAndStatus() {
        //given
        testCallback.assignMember(memberId);
        testCallback.changeStatusToInProgress();

        //when
        boolean result = callbackRepository.existsByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS);

        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("콜백에 할당된 멤버id와 상태를 매게변수로한 조회 - 실패(멤버id 아닌 경우)")
    void existsByAssignedMemberIdAndStatus_fail1() {
        //given
        testCallback.assignMember(memberId);
        testCallback.changeStatusToInProgress();

        //when
        boolean result = callbackRepository.existsByAssignedMemberIdAndStatus(memberId + 1L, Callback.Status.IN_PROGRESS);

        //then
        assertFalse(result);
    }

    @Test
    @DisplayName("콜백에 할당된 멤버id와 상태를 매게변수로한 조회 - 실패(멤버id만 틀리는 경우)")
    void existsByAssignedMemberIdAndStatus_fail2() {
        //given
        testCallback.assignMember(memberId);
        testCallback.changeStatusToInProgress();

        //when
        boolean result = callbackRepository.existsByAssignedMemberIdAndStatus(memberId + 1L, Callback.Status.IN_PROGRESS);

        //then
        assertFalse(result);
    }

    @Test
    @DisplayName("콜백에 할당된 멤버id와 상태를 매게변수로한 조회 - 실패(콜백 상태만 틀리는 경우)")
    void existsByAssignedMemberIdAndStatus_fail3() {
        //given
        testCallback.assignMember(memberId);
        testCallback.changeStatusToInProgress();

        //when
        boolean result = callbackRepository.existsByAssignedMemberIdAndStatus(memberId, Callback.Status.PENDING_COMPLETE);

        //then
        assertFalse(result);
    }

    @Test
    @DisplayName("콜백에 할당된 멤버id와 상태를 매게변수로한 조회 - 실패(멤버id, 콜백 상태 모두 틀리는 경우)")
    void existsByAssignedMemberIdAndStatus_fail4() {
        //given
        testCallback.assignMember(memberId);
        testCallback.changeStatusToInProgress();

        //when
        boolean result = callbackRepository.existsByAssignedMemberIdAndStatus(memberId + 1L, Callback.Status.WAITING);

        //then
        assertFalse(result);
    }
}
