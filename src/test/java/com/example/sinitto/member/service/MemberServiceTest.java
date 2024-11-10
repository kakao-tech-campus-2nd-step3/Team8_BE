package com.example.sinitto.member.service;

import com.example.sinitto.auth.service.TokenService;
import com.example.sinitto.callback.service.CallbackService;
import com.example.sinitto.common.exception.ConflictException;
import com.example.sinitto.helloCall.service.HelloCallService;
import com.example.sinitto.member.dto.RegisterResponse;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MockitoSettings
public class MemberServiceTest {
    @Mock
    MemberRepository memberRepository;
    @Mock
    TokenService tokenService;
    @Mock
    RedisTemplate<String, Object> redisTemplate;
    @Mock
    HashOperations<String, Object, Object> hashOperations;
    @InjectMocks
    MemberService memberService;
    @Mock
    ValueOperations<String, String> valueOperations;
    @Mock
    CallbackService callbackService;
    @Mock
    HelloCallService helloCallService;
    @Mock
    PointRepository pointRepository;
    @Mock
    PointLogRepository pointLogRepository;

    @Test
    @DisplayName("registerNewMember 메소드 테스트 - 시니또의 회원가입인 경우 환영 포인트 미지급")
    void registerNewMemberTest1() {
        //given
        String name = "testName";
        String phoneNumber = "01000000000";
        String email = "test@email.com";
        boolean isSinitto = true;

        when(memberRepository.existsByEmail(email)).thenReturn(false);

        //when
        RegisterResponse result = memberService.registerNewMember(name, phoneNumber, email, isSinitto);

        //then
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(pointRepository, never()).save(any());
        verify(pointLogRepository, never()).save(any());
        assertEquals(isSinitto, result.isSinitto());
    }

    @Test
    @DisplayName("registerNewMember 메소드 테스트 - 보호자의 회원가입인 경우 환영 포인트 지급")
    void registerNewMemberTest2() {
        //given
        String name = "testName";
        String phoneNumber = "01000000000";
        String email = "test@email.com";
        boolean isSinitto = false;

        when(memberRepository.existsByEmail(email)).thenReturn(false);

        //when
        RegisterResponse result = memberService.registerNewMember(name, phoneNumber, email, isSinitto);

        //then
        verify(memberRepository, times(1)).save(any(Member.class));
        verify(pointRepository, times(1)).save(any());
        verify(pointLogRepository, times(1)).save(any());
        assertEquals(isSinitto, result.isSinitto());
    }

    @Test
    @DisplayName("registerNewMember 메소드 테스트 - 이미 이메일이 있는 경우")
    void registerNewMemberTestWhenExists() {
        //given
        String name = "testName";
        String phoneNumber = "01000000000";
        String email = "test@email.com";
        boolean isSinitto = true;

        when(memberRepository.existsByEmail(email)).thenReturn(true);

        //when, then
        assertThrows(ConflictException.class, () -> memberService.registerNewMember(name, phoneNumber, email, isSinitto));
    }

    @Test
    @DisplayName("memberLogout 메소드 테스트")
    void memberLogoutTest() {
        //given
        Long memberId = 1L;
        String email = "test@email.com";
        Member member = mock(Member.class);
        String storedRefreshToken = "testRefreshToken";

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.getEmail()).thenReturn(email);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(redisTemplate.opsForHash().get(member.getEmail(), "refreshToken")).thenReturn(storedRefreshToken);
        //when
        memberService.memberLogout(memberId);

        //then
        verify(redisTemplate, times(1)).delete(member.getEmail());
    }

    @Test
    @DisplayName("memberLogout 메소드 테스트 - member를 찾을 수 없는 경우")
    void memberLogoutTestWhenMemberIsNull() {
        //given
        Long memberId = 1L;
        Member member = mock(Member.class);
        String storedRefreshToken = "testRefreshToken";

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NullPointerException.class, () -> memberService.memberLogout(memberId));
    }

    @Test
    @DisplayName("deleteMember 메소드 테스트")
    void deleteMemberTest() {
        //given
        Long memberId = 1L;
        String email = "test@email.com";
        Member member = mock(Member.class);
        String storedRefreshToken = "testRefreshToken";

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.getEmail()).thenReturn(email);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(redisTemplate.opsForHash().get(member.getEmail(), "refreshToken")).thenReturn(storedRefreshToken);

        //when
        memberService.deleteMember(memberId);

        //when
        verify(memberRepository, times(1)).deleteById(memberId);
    }

    @Test
    @DisplayName("deleteMember 메소드 테스트 - 탈퇴 신청한게 시니또인 경우")
    void deleteMemberTest2() {
        //given
        Long memberId = 1L;
        String email = "test@email.com";
        Member member = mock(Member.class);
        String storedRefreshToken = "testRefreshToken";

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.getEmail()).thenReturn(email);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(redisTemplate.opsForHash().get(member.getEmail(), "refreshToken")).thenReturn(storedRefreshToken);

        when(member.isSinitto()).thenReturn(true);
        //when
        memberService.deleteMember(memberId);

        //when
        verify(memberRepository, times(1)).deleteById(memberId);
        verify(callbackService, times(1)).cancelAssignedCallbackIfInProgress(any(Member.class));
        verify(helloCallService, times(1)).cancelAssignedHelloCallIfInProgress(any(Member.class));
    }

    @Test
    @DisplayName("deleteMember 메소드 테스트 - 탈퇴 신청한게 보호자인 경우")
    void deleteMemberTest3() {
        //given
        Long memberId = 1L;
        String email = "test@email.com";
        Member member = mock(Member.class);
        String storedRefreshToken = "testRefreshToken";

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(member.getEmail()).thenReturn(email);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(redisTemplate.opsForHash().get(member.getEmail(), "refreshToken")).thenReturn(storedRefreshToken);

        when(member.isSinitto()).thenReturn(false);
        //when
        memberService.deleteMember(memberId);

        //when
        verify(memberRepository, times(1)).deleteById(memberId);
        verify(callbackService, times(0)).cancelAssignedCallbackIfInProgress(any(Member.class));
        verify(helloCallService, times(0)).cancelAssignedHelloCallIfInProgress(any(Member.class));
    }
}
