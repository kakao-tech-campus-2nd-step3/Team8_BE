package com.example.sinitto.guard.service;

import com.example.sinitto.common.exception.BadRequestException;
import com.example.sinitto.guard.dto.GuardRequest;
import com.example.sinitto.guard.dto.GuardResponse;
import com.example.sinitto.guard.dto.SeniorRequest;
import com.example.sinitto.guard.dto.SeniorResponse;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@MockitoSettings
public class GuardServiceTest {
    @Mock
    MemberRepository memberRepository;
    @Mock
    SeniorRepository seniorRepository;
    @InjectMocks
    GuardService guardService;

    @Test
    @DisplayName("readGuide 메소드 테스트")
    void readGuardTest() {
        //given
        Member member = new Member("testName", "01012345678", "test@mail.com", false);
        Long memberId = 1L;
        Optional<Member> memberOptional = Optional.of(member);
        GuardResponse response = new GuardResponse(member.getName(), member.getEmail(), member.getPhoneNumber());
        when(memberRepository.findById(memberId)).thenReturn(memberOptional);

        //when
        GuardResponse result = guardService.readGuard(memberId);

        //then
        assertEquals(member.getName(), result.name());
        assertEquals(member.getEmail(), result.email());
        assertEquals(member.getPhoneNumber(), result.phoneNumber());
    }

    @Test
    @DisplayName("updateGuard 메소드 테스트")
    void updateGuardTest() {
        //given
        Member member = mock(Member.class);
        Long memberId = 1L;
        Optional<Member> memberOptional = Optional.of(member);
        GuardRequest guardRequest = new GuardRequest("newTestName", "01087654321");

        when(memberRepository.findById(memberId)).thenReturn(memberOptional);

        //when
        guardService.updateGuard(memberId, guardRequest);

        //then
        verify(member, times(1)).updateMember(guardRequest.name(), guardRequest.phoneNumber());
    }

    @Test
    @DisplayName("deleteGuard 메소드 테스트")
    void deleteGuardTest() {
        // Given
        Long memberId = 1L;
        Member member = new Member("testName", "01012345678", "test@mail.com", false);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // When
        guardService.deleteGuard(memberId);

        // Then
        verify(memberRepository, times(1)).delete(member);
    }

    @Test
    @DisplayName("createSenior 메소드 테스트")
    void createSeniorTest() {
        // Given
        Long memberId = 1L;
        Member member = new Member("testName", "01012345678", "test@mail.com", false);
        SeniorRequest seniorRequest = new SeniorRequest("testSeniorName", "01011111111");
        Senior senior = new Senior(seniorRequest.seniorName(), seniorRequest.seniorPhoneNumber(), member);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(seniorRepository.save(any(Senior.class))).thenReturn(senior);
        // When
        guardService.createSenior(memberId, seniorRequest);

        // Then
        verify(seniorRepository, times(1)).save(any(Senior.class));
    }

    @Test
    @DisplayName("createSenior 테스트 - 시니또일 경우 BadRequestException 발생")
    void createSeniorTestWhenSinittoDoThis() {
        // Given
        Long memberId = 1L;
        Member member = new Member("testName", "01012345678", "test@mail.com", true);
        SeniorRequest seniorRequest = new SeniorRequest("testSeniorName", "01011111111");
        Senior senior = new Senior(seniorRequest.seniorName(), seniorRequest.seniorPhoneNumber(), member);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // When, Then
        assertThrows(BadRequestException.class, () -> guardService.createSenior(memberId, seniorRequest));
    }

    @Test
    @DisplayName("createSenior 테스트 - 이미 등록된 번호로 시니어 생성하면 예외를 발생시켜야한다.")
    void createSeniorTestFailWhenDuplicatedPhoneNumber() {
        // Given
        Long memberId = 1L;
        Member member = new Member("testName", "01012345678", "test@mail.com", false);
        SeniorRequest seniorRequest = new SeniorRequest("testSeniorName", "01011111111");
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(seniorRepository.existsByPhoneNumber("01011111111")).thenReturn(true);

        // When, Then
        assertThrows(BadRequestException.class, () -> guardService.createSenior(memberId, seniorRequest));
    }

    @Test
    @DisplayName("readSeniors 메소드 테스트")
    void readSeniorsTest() {
        //given
        Long memberId = 1L;
        Member member = new Member("testName", "01012345678", "test@mail.com", true);

        List<Senior> seniorList = new ArrayList<>();
        seniorList.add(new Senior("testSenior1", "01011111111", member));
        seniorList.add(new Senior("testSenior2", "01022222222", member));
        seniorList.add(new Senior("testSenior3", "01033333333", member));

        when(seniorRepository.findByMemberId(memberId)).thenReturn(seniorList);

        //when
        List<SeniorResponse> result = guardService.readSeniors(memberId);

        //then
        assertEquals(result.getFirst().seniorName(), seniorList.getFirst().getName());
        assertEquals(result.getFirst().seniorPhoneNumber(), seniorList.getFirst().getPhoneNumber());
        assertEquals(result.get(1).seniorName(), seniorList.get(1).getName());
        assertEquals(result.get(1).seniorPhoneNumber(), seniorList.get(1).getPhoneNumber());
        assertEquals(result.get(2).seniorName(), seniorList.get(2).getName());
        assertEquals(result.get(2).seniorPhoneNumber(), seniorList.get(2).getPhoneNumber());
    }

    @Test
    @DisplayName("readOneSenior 메소드 테스트")
    void readOneSeniorTest() {
        //given
        Long memberId = 1L;
        Long seniorId = 2L;
        Member member = new Member("testName", "01012345678", "test@mail.com", true);
        Senior senior = new Senior("testSeniorName", "01000000000", member);

        when(seniorRepository.findByIdAndMemberId(seniorId, memberId)).thenReturn(Optional.of(senior));
        //when
        SeniorResponse result = guardService.readOneSenior(memberId, seniorId);

        //then
        assertEquals(result.seniorName(), senior.getName());
        assertEquals(result.seniorPhoneNumber(), senior.getPhoneNumber());
    }

    @Test
    @DisplayName("updateSenior 메소드 테스트")
    void updateSeniorTest() {
        //given
        Long memberId = 1L;
        Long seniorId = 2L;
        Member member = new Member("testName", "01012345678", "test@mail.com", true);
        Senior senior = mock(Senior.class);
        SeniorRequest request = new SeniorRequest("newSeniorName", "01011111111");

        when(seniorRepository.findByIdAndMemberId(seniorId, memberId)).thenReturn(Optional.of(senior));
        when(senior.getPhoneNumber()).thenReturn("01012121212");
        //when
        guardService.updateSenior(memberId, seniorId, request);

        //then
        verify(senior, times(1)).updateSenior(request.seniorName(), request.seniorPhoneNumber());
    }

    @Test
    @DisplayName("updateSenior 메소드 테스트 - 이미 등록된 번호로 수정시 예외가 발생해야한다.")
    void updateSeniorTestFailWhenDuplicatedPhoneNumber() {
        //given
        Long memberId = 1L;
        Long seniorId = 2L;
        Member member = new Member("testName", "01012345678", "test@mail.com", true);
        Senior senior = mock(Senior.class);
        SeniorRequest request = new SeniorRequest("newSeniorName", "01011111111");

        when(seniorRepository.findByIdAndMemberId(seniorId, memberId)).thenReturn(Optional.of(senior));
        when(seniorRepository.existsByPhoneNumber("01011111111")).thenReturn(true);
        when(senior.getPhoneNumber()).thenReturn("01012121212");
        //when then
        assertThrows(BadRequestException.class, () -> guardService.updateSenior(memberId, seniorId, request));
    }

    @Test
    @DisplayName("deleteSenior 메소드 테스트")
    void deleteSeniorTest() {
        //given
        Long memberId = 1L;
        Long seniorId = 2L;
        Member member = new Member("testName", "01012345678", "test@mail.com", true);
        Senior senior = new Senior("testSeniorName", "01000000000", member);

        when(seniorRepository.findByIdAndMemberId(seniorId, memberId)).thenReturn(Optional.of(senior));
        //when
        guardService.deleteSenior(memberId, seniorId);

        //then
        verify(seniorRepository, times(1)).delete(senior);
    }
}
