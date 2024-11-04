package com.example.sinitto.hellocall.service;

import com.example.sinitto.common.exception.BadRequestException;
import com.example.sinitto.common.exception.ConflictException;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.common.exception.UnauthorizedException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.helloCall.dto.*;
import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.HelloCallTimeLog;
import com.example.sinitto.helloCall.entity.TimeSlot;
import com.example.sinitto.helloCall.repository.HelloCallRepository;
import com.example.sinitto.helloCall.repository.HelloCallTimeLogRepository;
import com.example.sinitto.helloCall.repository.TimeSlotRepository;
import com.example.sinitto.helloCall.service.HelloCallService;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
public class HelloCallServiceTest {
    @Mock
    HelloCallRepository helloCallRepository;
    @Mock
    TimeSlotRepository timeSlotRepository;
    @Mock
    SeniorRepository seniorRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    HelloCallTimeLogRepository helloCallTimeLogRepository;
    @Mock
    PointService pointService;
    @InjectMocks
    HelloCallService helloCallService;

    @Test
    @DisplayName("createHelloCallByGuard 메소드 테스트")
    void createHelloCallByGuard() {
        //given
        Long memberId = 1L;
        Senior senior = mock(Senior.class);
        List<HelloCallRequest.TimeSlot> timeSlots = new ArrayList<>();
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        String dayName = "";

        switch (dayOfWeek) {
            case DayOfWeek.MONDAY -> dayName = "월";
            case DayOfWeek.TUESDAY -> dayName = "화";
            case DayOfWeek.WEDNESDAY -> dayName = "수";
            case DayOfWeek.THURSDAY -> dayName = "목";
            case DayOfWeek.FRIDAY -> dayName = "금";
            case DayOfWeek.SATURDAY -> dayName = "토";
            case DayOfWeek.SUNDAY -> dayName = "일";
        }
        timeSlots.add(new HelloCallRequest.TimeSlot(dayName, LocalTime.now().plusHours(1), LocalTime.now().plusHours(1)));
        HelloCallRequest helloCallRequest = new HelloCallRequest(senior.getId(), LocalDate.now(), LocalDate.now().plusDays(10), timeSlots, 1000, 10, "testRequirement");

        when(seniorRepository.findByIdAndMemberId(helloCallRequest.seniorId(), memberId)).thenReturn(Optional.of(senior));
        when(helloCallRepository.existsBySeniorAndStatusIn(senior, List.of(HelloCall.Status.WAITING, HelloCall.Status.IN_PROGRESS))).thenReturn(false);

        //when
        helloCallService.createHelloCallByGuard(memberId, helloCallRequest);

        //then
        verify(helloCallRepository, times(1)).save(any(HelloCall.class));
        verify(timeSlotRepository, times(helloCallRequest.timeSlots().size())).save(any(TimeSlot.class));
    }

    @Test
    @DisplayName("createHelloCallByGuard 메소드 테스트 - 시니어를 찾을 수 없을 때")
    void createHelloCallByGuardWhenSeniorIsNull() {
        //given
        Long memberId = 1L;
        Senior senior = mock(Senior.class);
        List<HelloCallRequest.TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new HelloCallRequest.TimeSlot("월", LocalTime.now(), LocalTime.now().plusHours(2)));
        HelloCallRequest helloCallRequest = new HelloCallRequest(senior.getId(), LocalDate.now(), LocalDate.now().plusDays(7), timeSlots, 1000, 10, "testRequirement");

        when(seniorRepository.findByIdAndMemberId(helloCallRequest.seniorId(), memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.createHelloCallByGuard(memberId, helloCallRequest));
    }

    @Test
    @DisplayName("createHelloCallByGuard 메소드 테스트 - 안부 전화 서비스가 존재할 때")
    void createHelloCallByGuardWhenHelloCallAlreadyExists() {
        //given
        Long memberId = 1L;
        Senior senior = mock(Senior.class);
        List<HelloCallRequest.TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new HelloCallRequest.TimeSlot("월", LocalTime.now(), LocalTime.now().plusHours(2)));
        HelloCallRequest helloCallRequest = new HelloCallRequest(senior.getId(), LocalDate.now(), LocalDate.now().plusDays(7), timeSlots, 1000, 10, "testRequirement");

        when(seniorRepository.findByIdAndMemberId(helloCallRequest.seniorId(), memberId)).thenReturn(Optional.of(senior));
        when(helloCallRepository.existsBySeniorAndStatusIn(senior, List.of(HelloCall.Status.WAITING, HelloCall.Status.IN_PROGRESS))).thenReturn(true);

        //when, /then
        assertThrows(ConflictException.class, () -> helloCallService.createHelloCallByGuard(memberId, helloCallRequest));
    }

    @Test
    @DisplayName("createHelloCallByGuard 메소드 테스트 - 포인트 부족할 때")
    void createHelloCallByGuardWhenPointIsLessThanPrice() {
        //given
        Long memberId = 1L;
        Senior senior = mock(Senior.class);
        List<HelloCallRequest.TimeSlot> timeSlots = new ArrayList<>();
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        String dayName = "";

        switch (dayOfWeek) {
            case DayOfWeek.MONDAY -> dayName = "월";
            case DayOfWeek.TUESDAY -> dayName = "화";
            case DayOfWeek.WEDNESDAY -> dayName = "수";
            case DayOfWeek.THURSDAY -> dayName = "목";
            case DayOfWeek.FRIDAY -> dayName = "금";
            case DayOfWeek.SATURDAY -> dayName = "토";
            case DayOfWeek.SUNDAY -> dayName = "일";
        }
        timeSlots.add(new HelloCallRequest.TimeSlot(dayName, LocalTime.now(), LocalTime.now().plusHours(2)));
        HelloCallRequest helloCallRequest = new HelloCallRequest(senior.getId(), LocalDate.now(), LocalDate.now().plusDays(7), timeSlots, 1000, 10, "testRequirement");

        when(seniorRepository.findByIdAndMemberId(helloCallRequest.seniorId(), memberId)).thenReturn(Optional.of(senior));
        when(helloCallRepository.existsBySeniorAndStatusIn(senior, List.of(HelloCall.Status.WAITING, HelloCall.Status.IN_PROGRESS))).thenReturn(false);

        //when, then
        assertThrows(BadRequestException.class, () -> helloCallService.createHelloCallByGuard(memberId, helloCallRequest));
    }

    @Test
    @DisplayName("readAllHelloCallsByGuard 메소드 테스트")
    void readAllHelloCallsByGuardTest() {
        //given
        Member member = mock(Member.class);
        Long memberId = 1L;
        List<Senior> seniors = new ArrayList<>();
        seniors.add(new Senior("testName", "01000000000", member));
        List<HelloCall> helloCalls = new ArrayList<>();
        helloCalls.add(new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 1000, 10, "testRequirement", seniors.getFirst()));

        when(seniorRepository.findByMemberId(memberId)).thenReturn(seniors);
        when(helloCallRepository.findAllBySeniorIn(seniors)).thenReturn(helloCalls);

        //when
        List<HelloCallResponse> result = helloCallService.readAllHelloCallsByGuard(memberId);

        //then
        assertEquals(result.getFirst().seniorName(), seniors.getFirst().getName());
    }

    @Test
    @DisplayName("readAllWaitingHelloCallsBySinitto 메소드 테스트")
    void readAllWaitingHelloCallsBySinitto() {
        //given
        Pageable pageable = PageRequest.of(0, 10);
        Senior senior = mock(Senior.class);
        List<HelloCall> helloCalls = new ArrayList<>();
        helloCalls.add(new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior));

        when(helloCallRepository.findAll()).thenReturn(helloCalls);

        //when
        Page<HelloCallResponse> result = helloCallService.readAllWaitingHelloCallsBySinitto(pageable);

        //then
        assertEquals(helloCalls.getFirst().getSenior().getName(), result.getContent().getFirst().seniorName());
        assertEquals(HelloCall.Status.WAITING, result.getContent().getFirst().status());
    }

    @Test
    @DisplayName("readHelloCallDetail 메소드 테스트")
    void readHelloCallDetail() {
        //given
        Senior senior = mock(Senior.class);
        Long helloCallId = 1L;
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));

        //when
        HelloCallDetailResponse result = helloCallService.readHelloCallDetail(helloCallId);

        //then
        assertEquals(result.seniorName(), senior.getName());
        assertEquals(result.seniorPhoneNumber(), senior.getPhoneNumber());
        assertEquals(result.startDate(), helloCall.getStartDate());
        assertEquals(result.endDate(), helloCall.getEndDate());
        assertEquals(result.requirement(), helloCall.getRequirement());
        assertEquals(result.price(), helloCall.getPrice());
    }

    @Test
    @DisplayName("readHelloCallDetail 메소드 테스트 - HelloCall을 찾을 수 없을 때")
    void readHelloCallDetailWhenHelloCallIsNotExist() {
        //given
        Long helloCallId = 1L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.readHelloCallDetail(helloCallId));
    }

    @Test
    @DisplayName("updateHelloCallByGuard 메소드 테스트 - HelloCall이 없을 때")
    void updateHelloCallByGuardTestWhenHelloCallIsNotExist() {
        //given
        Long memberId = 1L;
        Long helloCallId = 2L;
        List<HelloCallDetailUpdateRequest.TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new HelloCallDetailUpdateRequest.TimeSlot("월", LocalTime.now(), LocalTime.now().plusHours(2)));
        HelloCallDetailUpdateRequest helloCallDetailUpdateRequest = new HelloCallDetailUpdateRequest(LocalDate.now(), LocalDate.now().plusDays(7), timeSlots, 1000, 10, "testRequirement");

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.updateHelloCallByGuard(memberId, helloCallId, helloCallDetailUpdateRequest));
    }

    @Test
    @DisplayName("updateHelloCallByGuard 메소드 테스트 - member가 없을 때")
    void updateHelloCallByGuardTestWhenMemberIsNotExist() {
        //given
        Member member = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        Long helloCallId = 2L;
        List<HelloCallDetailUpdateRequest.TimeSlot> timeSlots = new ArrayList<>();
        timeSlots.add(new HelloCallDetailUpdateRequest.TimeSlot("월", LocalTime.now(), LocalTime.now().plusHours(2)));
        HelloCallDetailUpdateRequest helloCallDetailUpdateRequest = new HelloCallDetailUpdateRequest(LocalDate.now(), LocalDate.now().plusDays(7), timeSlots, 1000, 10, "testRequirement");
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.updateHelloCallByGuard(memberId, helloCallId, helloCallDetailUpdateRequest));
    }

    @Test
    @DisplayName("deleteHellCallByGuard 메소드 테스트")
    void deleteHellCallByGuardTest() {
        //given
        Member member = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        //when
        helloCallService.deleteHellCallByGuard(memberId, helloCallId);

        //then
        verify(helloCallRepository, times(1)).delete(any(HelloCall.class));
    }

    @Test
    @DisplayName("deleteHellCallByGuard 메소드 테스트 - HelloCall을 조회할 수 없을 때")
    void deleteHellCallByGuardTestWhenHelloCallIsNotExist() {
        //given
        Long memberId = 1L;
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.deleteHellCallByGuard(memberId, helloCallId));
    }

    @Test
    @DisplayName("deleteHellCallByGuard 메소드 테스트 - 멤버를 조회할 수 없을 때")
    void deleteHellCallByGuardTestWhenMemberIsNotExist() {
        //given
        Member member = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.deleteHellCallByGuard(memberId, helloCallId));
    }

    @Test
    @DisplayName("readHelloCallTimeLogByGuard 메소드 테스트")
    void readHelloCallTimeLogByGuardTest() {
        //given
        Member member = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        List<HelloCallTimeLog> helloCallTimeLogs = new ArrayList<>();
        helloCallTimeLogs.add(mock(HelloCallTimeLog.class));

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(helloCallTimeLogRepository.findAllByHelloCallId(helloCallId)).thenReturn(helloCallTimeLogs);

        //when
        List<HelloCallTimeLogResponse> result = helloCallService.readHelloCallTimeLogByGuard(memberId, helloCallId);

        //then
        assertEquals(result.getFirst().startTime(), helloCallTimeLogs.getFirst().getStartDateAndTime());
        assertEquals(result.getFirst().endTime(), helloCallTimeLogs.getFirst().getEndDateAndTime());
    }

    @Test
    @DisplayName("readHelloCallTimeLogByGuard 메소드 테스트 - HelloCall을 찾을 수 없을 때")
    void readHelloCallTimeLogByGuardTestWhenHelloCallIsNotExist() {
        //given
        Long memberId = 1L;
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.readHelloCallTimeLogByGuard(memberId, helloCallId));
    }

    @Test
    @DisplayName("readHelloCallTimeLogByGuard 메소드 테스트 - Member를 찾을 수 없을 때")
    void readHelloCallTimeLogByGuardTestWhenMemberIsNotExist() {
        //given
        Member member = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.readHelloCallTimeLogByGuard(memberId, helloCallId));
    }

    @Test
    @DisplayName("readHelloCallReportByGuard 메소드 테스트")
    void readHelloCallReportByGuardTest() {
        //given
        Member member = mock(Member.class);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);
        helloCall.setReport("testReport");

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));

        //when
        HelloCallReportResponse result = helloCallService.readHelloCallReportByGuard(memberId, helloCallId);

        //then
        assertEquals(result.startDate(), helloCall.getStartDate());
        assertEquals(result.endDate(), helloCall.getEndDate());
        assertEquals(result.sinittoName(), helloCall.getMemberName());
        assertEquals(result.report(), helloCall.getReport());
    }

    @Test
    @DisplayName("readHelloCallReportByGuard 메소드 테스트 - Member가 없는 경우")
    void readHelloCallReportByGuardTestWhenMemberIsNotExist() {
        //given
        Long memberId = 1L;
        Long helloCallId = 2L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.readHelloCallReportByGuard(memberId, helloCallId));
    }

    @Test
    @DisplayName("readHelloCallReportByGuard 메소드 테스트 - HelloCall이 없는 경우")
    void readHelloCallReportByGuardTestWhenHelloCallIsNotExist() {
        //given
        Member member = mock(Member.class);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);
        helloCall.setReport(null);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.readHelloCallReportByGuard(memberId, helloCallId));
    }

    @Test
    @DisplayName("readHelloCallReportByGuard 메소드 테스트 - report가 없는 경우")
    void readHelloCallReportByGuardTestWhenReportIsNotExist() {
        //given
        Member member = mock(Member.class);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);
        helloCall.setReport(null);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));

        //when, then
        assertThrows(BadRequestException.class, () -> helloCallService.readHelloCallReportByGuard(memberId, helloCallId));
    }

    @Test
    @DisplayName("makeCompleteHelloCallByGuard 메소드 테스트")
    void makeCompleteHelloCallByGuardTest() {
        Member member = mock(Member.class);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);
        helloCall.changeStatusToInProgress();
        helloCall.changeStatusToPendingComplete();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));

        //when
        helloCallService.makeCompleteHelloCallByGuard(memberId, helloCallId);

        // then
        verify(pointService, atLeastOnce()).earnPoint(anyLong(), anyInt(), any(PointLog.Content.class));
    }

    @Test
    @DisplayName("makeCompleteHelloCallByGuard 메소드 테스트 - Member가 없을 때")
    void makeCompleteHelloCallByGuardTestWhenMemberIsNotWaiting() {
        Long memberId = 1L;
        Long helloCallId = 2L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.makeCompleteHelloCallByGuard(memberId, helloCallId));
    }

    @Test
    @DisplayName("makeCompleteHelloCallByGuard 메소드 테스트 - HelloCall이 없을 때")
    void makeCompleteHelloCallByGuardTestWhenHelloCallIsNotWaiting() {
        Member member = mock(Member.class);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.makeCompleteHelloCallByGuard(memberId, helloCallId));
    }

    @Test
    @DisplayName("makeCompleteHelloCallByGuard 메소드 테스트 - 상태가 완료 대기가 아닐 때")
    void makeCompleteHelloCallByGuardTestWhenStatusIsNotWaiting() {
        Member member = mock(Member.class);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));

        //when, then
        assertThrows(BadRequestException.class, () -> helloCallService.makeCompleteHelloCallByGuard(memberId, helloCallId));
    }

    @Test
    @DisplayName("readAllHelloCallReportByAdmin 메소드 테스트")
    void readAllHelloCallReportByAdminTest() {
        Senior senior = mock(Senior.class);
        Member sinitto = mock(Member.class);
        List<HelloCall> helloCalls = new ArrayList<>();
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        helloCall.setMember(sinitto);
        helloCall.setReport("testReport");
        helloCalls.add(helloCall);

        when(helloCallRepository.findAll()).thenReturn(helloCalls);

        //when
        List<HelloCallReportResponse> result = helloCallService.readAllHelloCallReportByAdmin();

        // then
        assertEquals(helloCalls.getFirst().getMemberName(), result.getFirst().sinittoName());
        assertEquals(helloCalls.getFirst().getStartDate(), result.getFirst().startDate());
        assertEquals(helloCalls.getFirst().getEndDate(), result.getFirst().endDate());
        assertEquals(helloCalls.getFirst().getReport(), result.getFirst().report());
        assertEquals(helloCalls.getFirst().getStatus(), HelloCall.Status.WAITING);
    }

    @Test
    @DisplayName("acceptHelloCallBySinitto 메소드 테스트 - HelloCall이 없을 때")
    void acceptHelloCallBySinittoTestWhenHelloCallIsNotExist() {
        Long memberId = 1L;
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.acceptHelloCallBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("acceptHelloCallBySinitto 메소드 테스트 - Member가 없을 때")
    void acceptHelloCallBySinittoTestWhenMemberIsNotExist() {
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.acceptHelloCallBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("acceptHelloCallBySinitto 메소드 테스트 - 시니또가 아닐 때")
    void acceptHelloCallBySinittoTestWhenMemberIsNotSinitto() {
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        //when, then
        assertThrows(UnauthorizedException.class, () -> helloCallService.acceptHelloCallBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("writeHelloCallStartTimeBySinitto 메소드 테스트")
    void writeHelloCallStartTimeBySinittoTest() {
        //given
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);
        Optional<HelloCallTimeLog> recentLog = Optional.of(new HelloCallTimeLog(mock(HelloCall.class), sinitto, LocalDateTime.now(), LocalDateTime.now().plusDays(3)));

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(sinitto));
        when(helloCallTimeLogRepository.findTopByMemberAndHelloCallOrderByStartDateAndTimeDesc(sinitto, helloCall)).thenReturn(recentLog);

        //when
        helloCallService.writeHelloCallStartTimeBySinitto(memberId, helloCallId);

        //then
        verify(helloCallTimeLogRepository, times(1)).save(any(HelloCallTimeLog.class));
    }

    @Test
    @DisplayName("writeHelloCallStartTimeBySinitto 메소드 테스트 - 이미 진행중인 안부전화가 있을 때")
    void writeHelloCallStartTimeBySinittoTestWhenHelloCallAlreadyExists() {
        //given
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);
        Optional<HelloCallTimeLog> recentLog = Optional.of(new HelloCallTimeLog(mock(HelloCall.class), sinitto, LocalDateTime.now(), null));

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(sinitto));
        when(helloCallTimeLogRepository.findTopByMemberAndHelloCallOrderByStartDateAndTimeDesc(sinitto, helloCall)).thenReturn(recentLog);

        //when, then
        assertThrows(BadRequestException.class, () -> helloCallService.writeHelloCallStartTimeBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("writeHelloCallStartTimeBySinitto 메소드 테스트 - HelloCall이 없을 때")
    void writeHelloCallStartTimeBySinittoTestWhenHelloCallIsNotExist() {
        //given
        Long memberId = 1L;
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.writeHelloCallStartTimeBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("writeHelloCallStartTimeBySinitto 메소드 테스트 - Member가 없을 때")
    void writeHelloCallStartTimeBySinittoTestWhenMemberIsNotExist() {
        //given
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.writeHelloCallStartTimeBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("writeHelloCallEndTimeBySinitto 메소드 테스트")
    void writeHelloCallEndTimeBySinittoTest() {
        //given
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);
        HelloCallTimeLog helloCallTimeLog = new HelloCallTimeLog(helloCall, sinitto, LocalDateTime.now(), null);

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(sinitto));
        when(helloCallTimeLogRepository.findTopByMemberAndHelloCallOrderByStartDateAndTimeDesc(sinitto, helloCall)).thenReturn(Optional.of(helloCallTimeLog));

        //when
        helloCallService.writeHelloCallEndTimeBySinitto(memberId, helloCallId);

        //then
        Duration tolerance = Duration.ofMinutes(1);
        assertTrue(Duration.between(helloCallTimeLog.getEndDateAndTime(), LocalDateTime.now()).abs().compareTo(tolerance) <= 0);
    }

    @Test
    @DisplayName("writeHelloCallEndTimeBySinitto 메소드 테스트 - HelloCall이 없을 때")
    void writeHelloCallEndTimeBySinittoTestWhenHelloCallIsNotExist() {
        //given
        Long memberId = 1L;
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.writeHelloCallEndTimeBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("writeHelloCallEndTimeBySinitto 메소드 테스트 - Member가 없을 때")
    void writeHelloCallEndTimeBySinittoTestWhenMemberIsNotExist() {
        //given
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.writeHelloCallEndTimeBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("writeHelloCallEndTimeBySinitto 메소드 테스트 - 안부전화 로그를 찾을 수 없을 때")
    void writeHelloCallEndTimeBySinittoTestWhenHelloCallTimeLogIsNotExist() {
        //given
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(sinitto));
        when(helloCallTimeLogRepository.findTopByMemberAndHelloCallOrderByStartDateAndTimeDesc(sinitto, helloCall)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.writeHelloCallEndTimeBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("writeHelloCallEndTimeBySinitto 메소드 테스트 - 이미 종료된 안부전화일 때")
    void writeHelloCallEndTimeBySinittoTestWhenHelloCallAlreadyEnd() {
        //given
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);
        HelloCallTimeLog helloCallTimeLog = new HelloCallTimeLog(helloCall, sinitto, LocalDateTime.now(), LocalDateTime.now().plusDays(7));

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(sinitto));
        when(helloCallTimeLogRepository.findTopByMemberAndHelloCallOrderByStartDateAndTimeDesc(sinitto, helloCall)).thenReturn(Optional.of(helloCallTimeLog));

        //when, then
        assertThrows(BadRequestException.class, () -> helloCallService.writeHelloCallEndTimeBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("cancelHelloCallBySinitto 메소드 테스트")
    void cancelHelloCallBySinittoTest() {
        //given
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);
        helloCall.changeStatusToInProgress();

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(sinitto));

        //when
        helloCallService.cancelHelloCallBySinitto(memberId, helloCallId);

        //then
        assertNull(helloCall.getMember());
        assertEquals(helloCall.getStatus(), HelloCall.Status.WAITING);
    }

    @Test
    @DisplayName("cancelHelloCallBySinitto 메소드 테스트 - HelloCall이 없을 때")
    void cancelHelloCallBySinittoTestWhenHelloCallIsNotExist() {
        //given
        Long memberId = 1L;
        Long helloCallId = 2L;

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.cancelHelloCallBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("cancelHelloCallBySinitto 메소드 테스트 - Member가 없을 때")
    void cancelHelloCallBySinittoTestWhenMemberIsNotExist() {
        //given
        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(7), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        helloCall.setMember(sinitto);
        helloCall.changeStatusToInProgress();

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.cancelHelloCallBySinitto(memberId, helloCallId));
    }

    @Test
    @DisplayName("sendReportBySinitto 메소드 테스트")
    void sendReportBySinittoTest() {
        //given

        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now().minusDays(7), LocalDate.now().minusDays(1), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        HelloCallReportRequest helloCallReportRequest = new HelloCallReportRequest(helloCallId, "testReport");
        helloCall.setMember(sinitto);
        helloCall.changeStatusToInProgress();

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(sinitto));

        //when
        helloCallService.sendReportBySinitto(memberId, helloCallReportRequest);

        //then
        assertEquals(helloCall.getReport(), helloCallReportRequest.report());
    }

    @Test
    @DisplayName("sendReportBySinitto 메소드 테스트 - HelloCall이 없을 때")
    void sendReportBySinittoTestWhenHelloCallIsNotExist() {
        //given
        Long memberId = 1L;
        Long helloCallId = 2L;
        HelloCallReportRequest helloCallReportRequest = new HelloCallReportRequest(helloCallId, "testReport");

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.sendReportBySinitto(memberId, helloCallReportRequest));
    }

    @Test
    @DisplayName("sendReportBySinitto 메소드 테스트 - Member가 없을 때")
    void sendReportBySinittoTestWhenMemberIsNotExist() {
        //given

        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now().minusDays(7), LocalDate.now().plusDays(1), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        HelloCallReportRequest helloCallReportRequest = new HelloCallReportRequest(helloCallId, "testReport");
        helloCall.setMember(sinitto);
        helloCall.changeStatusToInProgress();

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.sendReportBySinitto(memberId, helloCallReportRequest));
    }

    @Test
    @DisplayName("sendReportBySinitto 메소드 테스트 - 종료 일자보다 빠르게 종료하려 할 때")
    void sendReportBySinittoTestWhenEndDateIsInvalid() {
        //given

        Member member = new Member("testName", "01000000000", "test@email.com", false);
        Member sinitto = mock(Member.class);
        Long memberId = 1L;
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        HelloCall helloCall = new HelloCall(LocalDate.now().minusDays(7), LocalDate.now().plusDays(1), 500, 10, "testRequirement", senior);
        Long helloCallId = 2L;
        HelloCallReportRequest helloCallReportRequest = new HelloCallReportRequest(helloCallId, "testReport");
        helloCall.setMember(sinitto);
        helloCall.changeStatusToInProgress();

        when(helloCallRepository.findById(helloCallId)).thenReturn(Optional.of(helloCall));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(sinitto));

        //when, then
        assertThrows(BadRequestException.class, () -> helloCallService.sendReportBySinitto(memberId, helloCallReportRequest));
    }

    @Test
    @DisplayName("readOwnHelloCallBySinitto 메소드 테스트")
    void readOwnHelloCallBySinittoTest() {
        //given
        Long memberId = 1L;
        Member member = mock(Member.class);
        Senior senior = new Senior("testSeniorName", "01012345678", member);
        List<HelloCall> helloCalls = new ArrayList<>();
        HelloCall helloCall = new HelloCall(LocalDate.now().minusDays(7), LocalDate.now().minusDays(3), 1000, 10, "testRequirement", senior);
        helloCall.setMember(member);
        helloCall.changeStatusToInProgress();
        helloCall.changeStatusToPendingComplete();
        helloCall.changeStatusToComplete();
        helloCalls.add(helloCall);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(helloCallRepository.findAllByMember(member)).thenReturn(helloCalls);

        //when
        List<HelloCallResponse> result = helloCallService.readOwnHelloCallBySinitto(memberId);

        //then
        assertEquals(result.getFirst().seniorName(), helloCalls.getFirst().getSenior().getName());
        assertEquals(result.getFirst().status(), helloCalls.getFirst().getStatus());
    }

    @Test
    @DisplayName("readOwnHelloCallBySinitto 메소드 테스트 - Member가 없을 때")
    void readOwnHelloCallBySinittoTestWhenMemberIsNotExist() {
        //given
        Long memberId = 1L;

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        //when, then
        assertThrows(NotFoundException.class, () -> helloCallService.readOwnHelloCallBySinitto(memberId));
    }
}
