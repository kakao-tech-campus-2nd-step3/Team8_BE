package com.example.sinitto.sinitto.service;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.sinitto.dto.SinittoBankRequest;
import com.example.sinitto.sinitto.dto.SinittoBankResponse;
import com.example.sinitto.sinitto.dto.SinittoRequest;
import com.example.sinitto.sinitto.dto.SinittoResponse;
import com.example.sinitto.sinitto.entity.SinittoBankInfo;
import com.example.sinitto.sinitto.repository.SinittoBankInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings
public class SinittoServiceTest {
    @Mock
    MemberRepository memberRepository;
    @Mock
    SinittoBankInfoRepository sinittoBankInfoRepository;
    @InjectMocks
    SinittoService sinittoService;

    @Test
    @DisplayName("createSinittoBankInfo 메소드 테스트")
    void createSinittoBankInfoTest() {
        //given
        Member member = new Member("testName", "01012345678", "test@mail.com", false);
        Long memberId = 1L;
        SinittoBankRequest sinittoBankRequest = new SinittoBankRequest("123456789", "testBankName");
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        //when
        sinittoService.createSinittoBankInfo(memberId, sinittoBankRequest);

        //then
        verify(sinittoBankInfoRepository, times(1)).save(any(SinittoBankInfo.class));
    }

    @Test
    @DisplayName("readSinitto 메소드 테스트")
    void readSinittoTest() {
        //given
        Member member = new Member("testName", "01012345678", "test@mail.com", false);
        Long memberId = 1L;
        SinittoBankRequest sinittoBankRequest = new SinittoBankRequest("123456789", "testBankName");
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        //when
        SinittoResponse result = sinittoService.readSinitto(memberId);

        //then
        assertEquals(member.getName(), result.name());
        assertEquals(member.getPhoneNumber(), result.phoneNumber());
        assertEquals(member.getEmail(), result.email());
    }

    @Test
    @DisplayName("readSinittoBankInfo 메소드 테스트")
    void readSinittoBankInfoTest() {
        //given
        Member member = new Member("testName", "01012345678", "test@mail.com", false);
        Long memberId = 1L;
        SinittoBankInfo sinittoBankInfo = new SinittoBankInfo("newTestBankName", "987654321", member);

        when(sinittoBankInfoRepository.findByMemberId(memberId)).thenReturn(Optional.of(sinittoBankInfo));

        //when
        SinittoBankResponse result = sinittoService.readSinittoBankInfo(memberId);

        //then
        assertEquals(result.accountNumber(), sinittoBankInfo.getAccountNumber());
        assertEquals(result.bankName(), sinittoBankInfo.getBankName());
    }

    @Test
    @DisplayName("readSinittoBankInfo 메소드 테스트 - sinittoBankInfo에 참조된 Member가 없을 때")
    void readSinittoBankInfoTestWithNull() {
        //given
        Member member = new Member("testName", "01012345678", "test@mail.com", false);
        Long memberId = 1L;

        when(sinittoBankInfoRepository.findByMemberId(memberId)).thenReturn(Optional.ofNullable(null));

        //when
        SinittoBankResponse result = sinittoService.readSinittoBankInfo(memberId);

        //then
        assertNull(result.accountNumber());
        assertNull(result.bankName());
    }

    @Test
    @DisplayName("updateSinitto 메소드 테스트")
    void updateSinittoTest() {
        //given
        Member member = new Member("testName", "01000000000", "test@email.com", true);
        Long memberId = 1L;
        SinittoRequest sinittoRequest = new SinittoRequest("newTestName", "01011111111");
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        //when
        sinittoService.updateSinitto(memberId, sinittoRequest);

        //then
        assertEquals(member.getName(), sinittoRequest.name());
        assertEquals(member.getPhoneNumber(), sinittoRequest.phoneNumber());
    }

    @Test
    @DisplayName("updateSinittoBankInfo 메소드 테스트")
    void updateSinittoBankInfo() {
        //given
        Member member = mock(Member.class);
        Long memberId = 1L;
        SinittoBankInfo sinittoBankInfo = new SinittoBankInfo("testBankName", "12345678", member);
        SinittoBankRequest sinittoBankRequest = new SinittoBankRequest("987654321", "newTestBankName");

        when(sinittoBankInfoRepository.findByMemberId(memberId)).thenReturn(Optional.of(sinittoBankInfo));

        //when
        sinittoService.updateSinittoBankInfo(memberId, sinittoBankRequest);

        //then
        assertEquals(sinittoBankInfo.getBankName(), sinittoBankRequest.bankName());
        assertEquals(sinittoBankInfo.getAccountNumber(), sinittoBankRequest.accountNumber());
    }
}
