package com.example.sinitto.callback.service;

import com.example.sinitto.callback.dto.CallbackResponse;
import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.exception.GuardMismatchException;
import com.example.sinitto.callback.exception.NotExistCallbackException;
import com.example.sinitto.callback.exception.NotMemberException;
import com.example.sinitto.callback.exception.NotSinittoException;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.callback.util.TwilioHelper;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
class CallbackServiceTest {

    @Mock
    CallbackRepository callbackRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    SeniorRepository seniorRepository;
    @InjectMocks
    CallbackService callbackService;

    @Test
    @DisplayName("getCallbacks - 성공")
    void getCallbacks() {
        //given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Member member = mock(Member.class);

        Callback callback = mock(Callback.class);
        when(callback.getId()).thenReturn(1L);
        when(callback.getPostTime()).thenReturn(LocalDateTime.now());
        when(callback.getStatus()).thenReturn(Callback.Status.WAITING.name());
        when(callback.getSeniorName()).thenReturn("James");
        when(callback.getSeniorId()).thenReturn(1L);
        Page<Callback> callbackPage = new PageImpl<>(List.of(callback));

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.isSinitto()).thenReturn(true);
        when(callbackRepository.findAll(pageable)).thenReturn(callbackPage);

        //when
        Page<CallbackResponse> result = callbackService.getCallbacks(memberId, pageable);

        //then
        assertEquals(1, result.getContent().size());
        assertEquals("James", result.getContent().get(0).seniorName());
    }

    @Test
    @DisplayName("getCallbacks 멤버가 없을때 - 실패")
    void getCallbacks_Fail_WhenNotMember() {
        //given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when then
        assertThrows(NotMemberException.class, () -> callbackService.getCallbacks(memberId, pageable));
    }

    @Test
    @DisplayName("getCallbacks 멤버가 시니또가 아닐때 - 실패")
    void getCallbacks_Fail_WhenNotSinitto() {
        //given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Member member = mock(Member.class);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.isSinitto()).thenReturn(false);

        //when then
        assertThrows(NotSinittoException.class, () -> callbackService.getCallbacks(memberId, pageable));
    }

    @Test
    @DisplayName("콜백 수락 - 성공")
    void accept() {
        //given
        Long memberId = 1L;
        Long callbackId = 1L;

        Member member = mock(Member.class);
        Callback callback = mock(Callback.class);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.isSinitto()).thenReturn(true);
        when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));

        //when
        callbackService.accept(memberId, callbackId);

        //then
        verify(callback).assignMember(memberId);
        verify(callback).changeStatusToInProgress();
    }

    @Test
    @DisplayName("콜백 완료 대기 - 성공")
    void complete() {
        //given
        Long memberId = 1L;
        Long callbackId = 1L;
        Long assignedMemberId = 1L;

        Member member = mock(Member.class);
        Callback callback = mock(Callback.class);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.isSinitto()).thenReturn(true);
        when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
        when(callback.getAssignedMemberId()).thenReturn(assignedMemberId);

        //when
        callbackService.pendingComplete(memberId, callbackId);

        //then
        verify(callback).changeStatusToPendingComplete();
    }

    @Test
    @DisplayName("수락한 콜백 취소 - 성공")
    void cancel() {
        //given
        Long memberId = 1L;
        Long callbackId = 1L;

        Member member = mock(Member.class);
        Callback callback = mock(Callback.class);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.isSinitto()).thenReturn(true);
        when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
        when(callback.getAssignedMemberId()).thenReturn(1L);

        //when
        callbackService.cancel(memberId, callbackId);

        //then
        verify(callback).cancelAssignment();
        verify(callback).changeStatusToWaiting();
    }

    @Test
    @DisplayName("새로운 콜백 등록")
    void addCallback() {
        //given
        String fromPhoneNumber = "+821012341234";
        String trimmedPhoneNumber = TwilioHelper.trimPhoneNumber(fromPhoneNumber);
        Senior senior = mock(Senior.class);

        when(seniorRepository.findByPhoneNumber(trimmedPhoneNumber)).thenReturn(Optional.of(senior));

        //when
        String result = callbackService.add(fromPhoneNumber);

        //then
        verify(callbackRepository, times(1)).save(any());
        assertNotNull(result);
    }

    @Test
    @DisplayName("새로운 콜백 등록할 때 전화온 번호가 등록된 번호가 아닐 때")
    void addCallback_fail() {
        //given
        String fromPhoneNumber = "+821012341234";
        String trimmedPhoneNumber = TwilioHelper.trimPhoneNumber(fromPhoneNumber);

        when(seniorRepository.findByPhoneNumber(trimmedPhoneNumber)).thenReturn(Optional.empty());

        //when
        String result = callbackService.add(fromPhoneNumber);

        //then
        verify(callbackRepository, times(0)).save(any());
        assertNotNull(result);
    }

    @Test
    @DisplayName("보호자가 콜백 대기 상태를 완료 생태로 변경 - 성공")
    void completeByGuard() {
        //given
        Long memberId = 1L;
        Long callbackId = 1L;
        Member member = mock(Member.class);
        Callback callback = mock(Callback.class);
        Senior senior = mock(Senior.class);

        when(member.getId()).thenReturn(1L);
        when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
        when(callback.getSenior()).thenReturn(senior);
        when(senior.getMember()).thenReturn(member);
        when(senior.getMember().getId()).thenReturn(1L);

        //when
        callbackService.complete(memberId, callbackId);

        //then
        verify(callback).changeStatusToComplete();
    }

    @Test
    @DisplayName("보호자가 콜백 대기 상태를 완료 생태로 변경 - 일치하는 보호자 ID가 아니어서 실패")
    void completeByGuard_fail() {
        //given
        Long memberId = 10L;
        Long callbackId = 1L;
        Member member = mock(Member.class);
        Callback callback = mock(Callback.class);
        Senior senior = mock(Senior.class);

        when(member.getId()).thenReturn(1L);
        when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
        when(callback.getSenior()).thenReturn(senior);
        when(senior.getMember()).thenReturn(member);
        when(senior.getMember().getId()).thenReturn(1L);

        //when then
        assertThrows(GuardMismatchException.class, () -> callbackService.complete(memberId, callbackId));
    }

    @Test
    @DisplayName("콜백이 할당된 시니또 조회에 성공")
    void getAcceptedCallback() {
        //given
        Long memberId = 1L;
        Callback callback = mock(Callback.class);
        Member member = mock(Member.class);

        //when
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.isSinitto()).thenReturn(true);
        when(callback.getId()).thenReturn(10L);

        when(callbackRepository.findByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS)).thenReturn(Optional.of(callback));
        CallbackResponse result = callbackService.getAcceptedCallback(memberId);

        //then
        assertEquals(10L, result.callbackId());
    }

    @Test
    @DisplayName("콜백이 할당된 시니또 조회에 실패")
    void getAcceptedCallback_fail() {
        //given
        Long memberId = 1L;
        Member member = mock(Member.class);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.isSinitto()).thenReturn(true);
        when(callbackRepository.findByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS)).thenReturn(Optional.empty());

        //when then
        assertThrows(NotExistCallbackException.class, () -> callbackService.getAcceptedCallback(memberId));
    }
}
