package com.example.sinitto.member.service;

import com.example.sinitto.auth.service.TokenService;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MockitoSettings
public class MemberTokenServiceTest {
    @Mock
    TokenService tokenService;
    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    MemberTokenService memberTokenService;

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
        Long result = memberTokenService.getMemberIdByToken(token);

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
        assertThrows(NotFoundException.class, () -> memberTokenService.getMemberIdByToken(token));
    }
}
