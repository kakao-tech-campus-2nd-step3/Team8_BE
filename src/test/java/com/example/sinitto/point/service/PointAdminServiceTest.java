package com.example.sinitto.point.service;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.dto.PointLogWithBankInfo;
import com.example.sinitto.point.dto.PointLogWithDepositMessage;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@MockitoSettings
class PointAdminServiceTest {

    @Mock
    private PointLogRepository pointLogRepository;
    @Mock
    private PointRepository pointRepository;
    @Mock
    private SinittoBankInfoRepository sinittoBankInfoRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private PointAdminService pointAdminService;

    @Nested
    @DisplayName("포인트 충전에 관한 관리자 페이지 테스트")
    class PointChargeAdminPageTest {

        @Test
        @DisplayName("포인트 충전 로그와 입금메시지 조회에 성공한다.")
        void getPointLogWithDepositMessage() {
            // given
            PointLog pointLog = mock(PointLog.class);
            Member member = mock(Member.class);
            when(pointLogRepository.findAllByStatusInOrderByPostTimeDesc(anyList())).thenReturn(List.of(pointLog));
            when(pointLog.getMember()).thenReturn(member);
            when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
            when(member.getDepositMessage()).thenReturn("DepositMessage");

            // when
            List<PointLogWithDepositMessage> result = pointAdminService.getPointLogWithDepositMessage();

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("DepositMessage", result.getFirst().depositMessage());
        }

        @Test
        @DisplayName("충전 요청 상태인 것을 충전 대기 상태로 전환시킨다.")
        void changeChargeLogToWaiting() {
            // given
            PointLog pointLog = mock(PointLog.class);
            when(pointLogRepository.findById(anyLong())).thenReturn(Optional.of(pointLog));

            // when
            pointAdminService.changeChargeLogToWaiting(1L);

            // then
            verify(pointLog).changeStatusToChargeWaiting();
        }

        @Test
        @DisplayName("충전 대기 상태인 것을 충전 완료 상태로 전환시킨다. 이때, 이것을 수행한 시니또에게 포인트가 동시에 적립이 된다.")
        void earnPointAndChangeToChargeComplete() {
            // given
            PointLog pointLog = mock(PointLog.class);
            Point point = mock(Point.class);
            Member member = mock(Member.class);
            when(pointLog.getMember()).thenReturn(member);
            when(pointLogRepository.findById(anyLong())).thenReturn(Optional.of(pointLog));
            when(pointRepository.findByMember(pointLog.getMember())).thenReturn(Optional.of(point));

            // when
            pointAdminService.earnPointAndChangeToChargeComplete(1L);

            // then
            verify(pointLog).changeStatusToChargeComplete();
            verify(point).earn(anyInt());
        }

        @Test
        @DisplayName("어떤 이유에 의해서 시니또가 한 충전 요청을 실패 표시한다.")
        void changeChargeLogToFail() {
            // Given
            PointLog pointLog = mock(PointLog.class);
            when(pointLogRepository.findById(anyLong())).thenReturn(Optional.of(pointLog));

            // When
            pointAdminService.changeChargeLogToFail(1L);

            // Then
            verify(pointLog).changeStatusToChargeFail();
        }

    }

    @Nested
    @DisplayName("포인트 출금에 관한 관리자 페이지 테스트")
    class PointWithdrawAdminPageTest {

        @Test
        @DisplayName("출금 요청 상태인 것을 출금 대기 상태로 전환시킨다.")
        void changeWithdrawLogToWaiting() {
            // given
            PointLog pointLog = mock(PointLog.class);
            when(pointLogRepository.findById(anyLong())).thenReturn(Optional.of(pointLog));

            // when
            pointAdminService.changeWithdrawLogToWaiting(1L);

            // then
            verify(pointLog).changeStatusToWithdrawWaiting();
        }

        @Test
        @DisplayName("출금 대기 상태인 것을 출금 완료 상태로 전환시킨다.")
        void changeWithdrawLogToComplete() {
            // given
            PointLog pointLog = mock(PointLog.class);
            when(pointLogRepository.findById(anyLong())).thenReturn(Optional.of(pointLog));

            // when
            pointAdminService.changeWithdrawLogToComplete(1L);

            // then
            verify(pointLog).changeStatusToWithdrawComplete();
        }

        @Test
        @DisplayName("어떤 이유에 의해서 시니또의 출금 요청을 실패 상태로 전환시킨다. 동시에 시니또에게 포인트를 돌려준다.")
        void changeWithdrawLogToFail() {
            // given
            PointLog pointLog = mock(PointLog.class);
            Point point = mock(Point.class);
            when(pointLogRepository.findById(anyLong())).thenReturn(Optional.of(pointLog));
            when(pointRepository.findByMember(pointLog.getMember())).thenReturn(Optional.of(point));

            // when
            pointAdminService.changeWithdrawLogToFail(1L);

            // then
            verify(pointLog).changeStatusToWithdrawFail();
            verify(point).earn(anyInt());
        }

        @Test
        @DisplayName("출금 로그와 시니또의 은행 정보 조회에 성공한다.")
        void getPointLogWithBankInfo() {
            // given
            PointLog pointLog = mock(PointLog.class);
            SinittoBankInfo sinittoBankInfo = mock(SinittoBankInfo.class);

            when(pointLogRepository.findAllByStatusInOrderByPostTimeDesc(anyList())).thenReturn(List.of(pointLog));
            when(sinittoBankInfoRepository.findByMemberId(anyLong())).thenReturn(Optional.of(sinittoBankInfo));

            when(pointLog.getMember()).thenReturn(mock(Member.class));
            when(pointLog.getPrice()).thenReturn(5050);
            when(sinittoBankInfo.getBankName()).thenReturn("BankName");
            when(sinittoBankInfo.getAccountNumber()).thenReturn("AccountNumber");

            // when
            List<PointLogWithBankInfo> result = pointAdminService.getPointLogWithBankInfo();

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(5050, result.getFirst().price());
            assertEquals("BankName", result.getFirst().bankName());
            assertEquals("AccountNumber", result.getFirst().bankAccountNumber());
        }
    }
}
