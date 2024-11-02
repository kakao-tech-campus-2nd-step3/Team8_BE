package com.example.sinitto.point.service;

import com.example.sinitto.common.exception.BadRequestException;
import com.example.sinitto.common.exception.ForbiddenException;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.dto.PointChargeResponse;
import com.example.sinitto.point.dto.PointLogResponse;
import com.example.sinitto.point.dto.PointResponse;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import com.example.sinitto.sinitto.repository.SinittoBankInfoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
class PointServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    PointLogRepository pointLogRepository;
    @Mock
    PointRepository pointRepository;
    @Mock
    SinittoBankInfoRepository sinittoBankInfoRepository;
    @InjectMocks
    PointService pointService;

    @Nested
    @DisplayName("포인트 조회 테스트")
    class GetPointTest {

        @Test
        @DisplayName("포인트 조회 성공한다.")
        void getPoint1() {
            //given
            Member member = mock(Member.class);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

            Point point = mock(Point.class);
            when(pointRepository.findByMember(member)).thenReturn(Optional.of(point));

            //when
            PointResponse result = pointService.getPoint(1L);

            //then
            assertInstanceOf(PointResponse.class, result);
        }

        @Test
        @DisplayName("우리 멤버가 아닌 사람이 요청시 예외를 발생시켜야한다.")
        void getPoint2() {
            //given
            when(memberRepository.findById(1L)).thenReturn(Optional.empty());

            //when then
            assertThrows(NotFoundException.class, () -> pointService.getPoint(1L));
        }

        @Test
        @DisplayName("우리 멤버는 맞지만 연관된 포인트 엔티티가 없으면 예외를 발생시켜야한다.")
        void getPoint3() {
            //given
            Member member = mock(Member.class);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

            when(pointRepository.findByMember(member)).thenReturn(Optional.empty());

            //when then
            assertThrows(NotFoundException.class, () -> pointService.getPoint(1L));
        }
    }

    @Nested
    @DisplayName("포인트 로그 조회 테스트")
    class GetPointLogTest {

        @Test
        @DisplayName("포인트 로그 조회 성공한다.")
        void getPointLogs1() {
            //given
            Member member = mock(Member.class);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

            PointLog pointLog = mock(PointLog.class);
            when(pointLog.getContent()).thenReturn("content");
            when(pointLog.getPrice()).thenReturn(10000);
            when(pointLog.getStatus()).thenReturn(PointLog.Status.EARN);

            when(pointLogRepository.findAllByMember(member, Pageable.ofSize(1))).thenReturn(new PageImpl<>(List.of(pointLog)));

            //when
            Page<PointLogResponse> result = pointService.getPointLogs(1L, Pageable.ofSize(1));

            //then
            assertNotNull(result);
            assertEquals(PointLog.Status.EARN, result.getContent().getFirst().status());

        }

        @Test
        @DisplayName("포인트 로그 요청을 우리 멤버가 아닌 사람이하면 예외를 발생시켜야한다.")
        void getPointLogs2() {
            //given
            Long memberId = 1L;
            when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

            //when then
            assertThrows(NotFoundException.class, () -> pointService.getPointLogs(memberId, Pageable.ofSize(1)));
        }

    }

    @Nested
    @DisplayName("포인트 충전 테스트")
    class SavePointTest {

        @Test
        @DisplayName("포인트 충전 요청 성공한다.")
        void savePointChargeRequest1() {
            //given
            Member member = mock(Member.class);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

            //when
            PointChargeResponse result = pointService.savePointChargeRequest(1L, 10000);

            //then
            verify(pointLogRepository).save(any(PointLog.class));
            assertNotNull(result);
        }

        @Test
        @DisplayName("포인트 충전 요청시 멤버가 존재하지 않으면 예외를 발생시켜야한다.")
        void savePointChargeRequest2() {
            //given
            when(memberRepository.findById(1L)).thenReturn(Optional.empty());

            //when then
            assertThrows(NotFoundException.class, () -> pointService.savePointChargeRequest(1L, 10000));
        }
    }

    @Nested
    @DisplayName("포인트 출금 테스트")
    class WithdrawPointTest {

        @Test
        @DisplayName("포인트 출금 요청 성공한다.")
        void savePointWithdrawRequest1() {
            //given
            Member member = mock(Member.class);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(true);
            when(sinittoBankInfoRepository.existsByMemberId(1L)).thenReturn(true);

            Point point = mock(Point.class);
            when(pointRepository.findByMember(member)).thenReturn(Optional.of(point));
            when(point.isSufficientForDeduction(10000)).thenReturn(true);

            //when
            pointService.savePointWithdrawRequest(1L, 10000);

            //then
            verify(point).deduct(10000);
            verify(pointLogRepository).save(any(PointLog.class));
        }

        @Test
        @DisplayName("시니또가 아닌 멤버가 출금 요청시 예외를 발생시켜야한다.")
        void savePointWithdrawRequest2() {
            //given
            Member member = mock(Member.class);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(false);

            //when then
            assertThrows(ForbiddenException.class, () -> pointService.savePointWithdrawRequest(1L, 10000));
        }

        @Test
        @DisplayName("시니또의 은행 계좌 정보가 없으면 예외를 발생시켜야한다.")
        void savePointWithdrawRequest3() {
            //given
            Member member = mock(Member.class);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(true);
            when(sinittoBankInfoRepository.existsByMemberId(1L)).thenReturn(false);

            //when then
            assertThrows(NotFoundException.class, () -> pointService.savePointWithdrawRequest(1L, 10000));
        }

        @Test
        @DisplayName("포인트가 충분하지 않으면 예외를 발생시켜야한다.")
        void savePointWithdrawRequest4() {
            //given
            Member member = mock(Member.class);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(true);
            when(sinittoBankInfoRepository.existsByMemberId(1L)).thenReturn(true);

            Point point = mock(Point.class);
            when(pointRepository.findByMember(member)).thenReturn(Optional.of(point));
            when(point.isSufficientForDeduction(10000)).thenReturn(false);

            //when then
            assertThrows(BadRequestException.class, () -> pointService.savePointWithdrawRequest(1L, 10000));
        }
    }
}
