package com.example.sinitto.point.service;

import com.example.sinitto.common.exception.BadRequestException;
import com.example.sinitto.common.exception.ConflictException;
import com.example.sinitto.common.exception.ForbiddenException;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.common.service.KakaoMessageService;
import com.example.sinitto.common.service.SlackMessageService;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.dto.PointChargeResponse;
import com.example.sinitto.point.dto.PointLogResponse;
import com.example.sinitto.point.dto.PointResponse;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import com.example.sinitto.sinitto.entity.SinittoBankInfo;
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
    @Mock
    KakaoMessageService kakaoMessageService;
    @Mock
    SlackMessageService slackMessageService;

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
            when(pointLog.getContent()).thenReturn(PointLog.Content.WELCOME_POINT);
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

        @Test
        @DisplayName("REQUEST or WAITING 상태인 포인트 충전 요청이 있으면 예외를 발생시킨다.")
        void savePointChargeRequest3() {
            //given
            Member member = mock(Member.class);
            when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
            when(pointLogRepository.existsByMemberAndStatusIn(member, List.of(PointLog.Status.CHARGE_REQUEST, PointLog.Status.CHARGE_WAITING))).thenReturn(true);

            //when then
            assertThrows(ConflictException.class, () -> pointService.savePointChargeRequest(1L, 10000));
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

            SinittoBankInfo sinittoBankInfo = mock(SinittoBankInfo.class);
            when(sinittoBankInfoRepository.findByMemberId(1L)).thenReturn(Optional.of(sinittoBankInfo));

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

        @Test
        @DisplayName("최소 출금 포인트보다 모자라면 예외를 발생시켜야한다.")
        void savePointWithdrawRequest5() {
            //given
            int withdrawPoint = 4999;

            //when then
            assertThrows(BadRequestException.class, () -> pointService.savePointWithdrawRequest(1L, withdrawPoint));
        }
    }

    @Nested
    @DisplayName("포인트 적립 테스트")
    class EarnPointTest {
        @Test
        @DisplayName("포인트 적립 성공한다.")
        void earnPoint1() {
            //given
            Point point = mock(Point.class);
            when(pointRepository.findByMemberId(1L)).thenReturn(Optional.of(point));

            //when
            pointService.earnPoint(1L, 10000, PointLog.Content.COMPLETE_HELLO_CALL_AND_EARN);

            //then
            verify(point).earn(10000);
            verify(pointLogRepository).save(any(PointLog.class));
        }

        @Test
        @DisplayName("멤버에 연관된 포인트가 없으면 예외를 발생시켜야한다.")
        void earnPoint2() {
            //given
            when(pointRepository.findByMemberId(1L)).thenReturn(Optional.empty());


            //when then
            assertThrows(NotFoundException.class, () -> pointService.earnPoint(1L, 10000, PointLog.Content.COMPLETE_CALLBACK_AND_EARN));
        }
    }

    @Nested
    @DisplayName("포인트 차감 테스트")
    class DeductPointTest {

        @Test
        @DisplayName("포인트 차감 성공한다.")
        void deductPoint1() {
            //given
            Point point = mock(Point.class);
            when(pointRepository.findByMemberIdWithWriteLock(1L)).thenReturn(Optional.of(point));
            when(point.isSufficientForDeduction(10000)).thenReturn(true);

            //when
            pointService.deductPoint(1L, 10000, PointLog.Content.SPEND_COMPLETE_CALLBACK);

            //then
            verify(point).deduct(10000);
            verify(pointLogRepository).save(any(PointLog.class));
        }

        @Test
        @DisplayName("멤버에 연관된 포인트가 없으면 예외를 발생시켜야한다.")
        void deductPoint2() {
            //given
            when(pointRepository.findByMemberIdWithWriteLock(1L)).thenReturn(Optional.empty());

            //when then

            assertThrows(NotFoundException.class, () -> pointService.deductPoint(1L, 10000, PointLog.Content.SPEND_COMPLETE_CALLBACK));
        }

        @Test
        @DisplayName("포인트가 부족하면 예외를 발생시켜야한다.")
        void deductPoint3() {

            //given
            Point point = mock(Point.class);
            when(pointRepository.findByMemberIdWithWriteLock(1L)).thenReturn(Optional.of(point));
            when(point.isSufficientForDeduction(10000)).thenReturn(false);

            //when then
            assertThrows(BadRequestException.class, () -> pointService.deductPoint(1L, 10000, PointLog.Content.SPEND_COMPLETE_HELLO_CALL));
        }
    }

    @Nested
    @DisplayName("포인트 환불 테스트")
    class RefundPointByDeleteTest {

        @Test
        @DisplayName("포인트 환불 성공한다. 성공하면 포인트가 되돌아 온다(적립)")
        void refundPointByDelete1() {
            //given
            Point point = mock(Point.class);
            when(pointRepository.findByMemberIdWithWriteLock(1L)).thenReturn(Optional.of(point));
            when(point.isSufficientForDeduction(10000)).thenReturn(true);

            //when
            pointService.refundPointByDelete(1L, 10000, PointLog.Content.SPEND_CANCEL_HELLO_CALL);

            //then
            verify(point).earn(10000);
            verify(pointLogRepository).save(any(PointLog.class));
        }


        @Test
        @DisplayName("멤버에 연관된 포인트가 없으면 예외를 발생시켜야한다.")
        void refundPointByDelete2() {
            //given
            when(pointRepository.findByMemberIdWithWriteLock(1L)).thenReturn(Optional.empty());

            //when then
            assertThrows(NotFoundException.class, () -> pointService.refundPointByDelete(1L, 10000, PointLog.Content.SPEND_CANCEL_HELLO_CALL));
        }

        @Test
        @DisplayName("포인트가 부족하면 예외를 발생시켜야한다.")
        void refundPointByDelete3() {
            //given
            Point point = mock(Point.class);
            when(pointRepository.findByMemberIdWithWriteLock(1L)).thenReturn(Optional.of(point));
            when(point.isSufficientForDeduction(10000)).thenReturn(false);

            //when then
            assertThrows(BadRequestException.class, () -> pointService.refundPointByDelete(1L, 10000, PointLog.Content.SPEND_CANCEL_HELLO_CALL));
        }
    }

}
