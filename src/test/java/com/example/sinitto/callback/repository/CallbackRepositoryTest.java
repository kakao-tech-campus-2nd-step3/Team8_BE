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

import java.time.LocalDateTime;
import java.util.List;
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
    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member("member", "01043214321", "tjdgns5506@gmai.com", true);
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


    @Test
    @DisplayName("findAllByStatusAndPendingCompleteTimeBefore 이용해서 2일이 지난 PendingComplete 콜백 조회 - 성공")
    void findAllByStatusAndPendingCompleteTimeBefore_success() {
        // given
        LocalDateTime beforeTime = LocalDateTime.of(2024, 1, 5, 13, 00).minusDays(2);
        Senior testSenior = seniorRepository.save(new Senior("senior", "01012341234", testMember));

        Callback callback1 = callbackRepository.save(new Callback(Callback.Status.PENDING_COMPLETE, testSenior));
        callback1.setPendingCompleteTime(LocalDateTime.of(2024, 1, 3, 12, 58));
        Callback callback2 = callbackRepository.save(new Callback(Callback.Status.PENDING_COMPLETE, testSenior));
        callback2.setPendingCompleteTime(LocalDateTime.of(2024, 1, 3, 12, 59));
        Callback callback3 = callbackRepository.save(new Callback(Callback.Status.PENDING_COMPLETE, testSenior));
        callback3.setPendingCompleteTime(LocalDateTime.of(2024, 1, 3, 12, 59));
        Callback callback4 = callbackRepository.save(new Callback(Callback.Status.PENDING_COMPLETE, testSenior));
        callback4.setPendingCompleteTime(LocalDateTime.of(2024, 1, 3, 13, 01));
        Callback callback5 = callbackRepository.save(new Callback(Callback.Status.PENDING_COMPLETE, testSenior));
        callback5.setPendingCompleteTime(LocalDateTime.of(2024, 1, 3, 13, 02));

        // when
        List<Callback> result = callbackRepository.findAllByStatusAndPendingCompleteTimeBefore(Callback.Status.PENDING_COMPLETE, beforeTime);

        // then
        assertEquals(3, result.size());
        assertTrue(result.contains(callback1));
        assertTrue(result.contains(callback2));
        assertTrue(result.contains(callback3));
    }

    @Test
    @DisplayName("findAllByStatusAndPendingCompleteTimeBefore 이용해서 2일이 지난 PendingComplete 콜백 조회 - 만약 아무것도 조건에 해당 안될 경우 테스트")
    void findAllByStatusAndPendingCompleteTimeBefore_success_zeroList() {
        // given
        LocalDateTime beforeTime = LocalDateTime.of(2024, 1, 5, 13, 00).minusDays(2);
        Senior testSenior = seniorRepository.save(new Senior("senior", "01012341234", testMember));

        Callback callback1 = callbackRepository.save(new Callback(Callback.Status.PENDING_COMPLETE, testSenior));
        callback1.setPendingCompleteTime(LocalDateTime.of(2024, 1, 3, 13, 01));
        Callback callback2 = callbackRepository.save(new Callback(Callback.Status.PENDING_COMPLETE, testSenior));
        callback2.setPendingCompleteTime(LocalDateTime.of(2024, 1, 3, 13, 02));

        // when
        List<Callback> result = callbackRepository.findAllByStatusAndPendingCompleteTimeBefore(Callback.Status.PENDING_COMPLETE, beforeTime);

        // then
        assertNotNull(result);
        assertEquals(0, result.size());

    }

    @Test
    @DisplayName("시니어와 상태들을 조건을 활용한 조회 테스트")
    void existsBySeniorAndStatusIn() {
        // given
        Senior testSenior = seniorRepository.save(new Senior("senior", "01012341234", testMember));

        callbackRepository.save(new Callback(Callback.Status.WAITING, testSenior));
        callbackRepository.save(new Callback(Callback.Status.PENDING_COMPLETE, testSenior));
        callbackRepository.save(new Callback(Callback.Status.COMPLETE, testSenior));

        // when
        boolean result1 = callbackRepository.existsBySeniorAndStatusIn(testSenior, List.of(Callback.Status.IN_PROGRESS, Callback.Status.WAITING));
        boolean result2 = callbackRepository.existsBySeniorAndStatusIn(testSenior, List.of(Callback.Status.WAITING));
        boolean result3 = callbackRepository.existsBySeniorAndStatusIn(testSenior, List.of(Callback.Status.IN_PROGRESS));
        boolean result4 = callbackRepository.existsBySeniorAndStatusIn(testSenior, List.of(Callback.Status.COMPLETE));

        // then
        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
        assertTrue(result4);
    }
}
