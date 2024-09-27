package com.example.sinitto.callback.service;

import com.example.sinitto.callback.dto.CallbackResponse;
import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.exception.NotSinittoException;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
class CallbackServiceTest {

    @Mock
    CallbackRepository callbackRepository;
    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    CallbackService callbackService;

    @Test
    void getCallbacks_Success() {
        //given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Member member = mock(Member.class);

        Callback callback = mock(Callback.class);
        when(callback.getId()).thenReturn(1L);
        when(callback.getPostTime()).thenReturn(LocalDateTime.now());
        when(callback.getStatus()).thenReturn("대기중");
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
    void getCallbacks_Fail_WhenNotMember() {
        //given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when then
        assertThrows(EntityNotFoundException.class, () -> callbackService.getCallbacks(memberId, pageable));
    }

    @Test
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
}
