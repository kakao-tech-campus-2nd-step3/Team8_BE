package com.example.sinitto.member.service;

import com.example.sinitto.auth.service.TokenService;
import com.example.sinitto.common.exception.ConflictException;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.member.dto.RegisterResponse;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.entity.Point;
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
    PointRepository pointRepository;
    @Mock
    TokenService tokenService;
    @Mock
    RedisTemplate<String, Object> redisTemplate;
    @Mock
    private HashOperations<String, Object, Object> hashOperations;
    @InjectMocks
    MemberService memberService;
    @Mock
    private ValueOperations<String, String> valueOperations;

    @Test
    @DisplayName("getMemberIdByToken 메소드 테스트")
    void getMemberIdByTokenTest() {
        //given
        String token = "testtoken";
        String email = "test@email.com";
        Member member = mock(Member.class);

        when(tokenService.extractEmailFromAccessToken(token)).thenReturn(email);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        //when
        Long result = memberService.getMemberIdByToken(token);

        //then
        assertEquals(result, member.getId());
    }

    @Test
    @DisplayName("getMemberIdByToken 메소드 테스트 - memberRepository에 없을 경우")
    void getMemberIdByTokenTestWhenNotInMemberRepository() {
        //given
        String token = "testtoken";
        String email = "test@email.com";

        when(tokenService.extractEmailFromAccessToken(token)).thenReturn(email);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> memberService.getMemberIdByToken(token));
    }

    @Test
    @DisplayName("registerNewMember 메소드 테스트")
    void registerNewMemberTest() {
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
        verify(pointRepository, times(1)).save(any(Point.class));
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
    void deleteMemberTest(){
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
}
