package com.example.sinitto.callback.service;

import com.example.sinitto.callback.dto.CallbackForSinittoResponse;
import com.example.sinitto.callback.dto.CallbackResponse;
import com.example.sinitto.callback.dto.CallbackUsageHistoryResponse;
import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.callback.util.TwilioHelper;
import com.example.sinitto.common.exception.ForbiddenException;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
class CallbackServiceTest {

    @Mock
    CallbackRepository callbackRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    SeniorRepository seniorRepository;
    @Mock
    PointService pointService;
    @InjectMocks
    CallbackService callbackService;

    @Test
    @DisplayName("보호자의 콜백 요청 내역 조회 테스트")
    void getCallbackHistoryOfGuard() {
        // given
        Long memberId = 1L;
        Member member = mock(Member.class);
        Senior senior = mock(Senior.class);
        List<Senior> seniors = List.of(senior);
        Callback callback = mock(Callback.class);
        Page<Callback> callbackPage = new PageImpl<>(List.of(callback));
        Pageable pageable = PageRequest.of(0, 10);

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(seniorRepository.findAllByMember(member)).thenReturn(seniors);
        when(callbackRepository.findAllBySeniorIn(seniors, pageable)).thenReturn(callbackPage);
        when(callback.getId()).thenReturn(1L);
        when(callback.getSeniorName()).thenReturn("SeniorName");
        when(callback.getPostTime()).thenReturn(LocalDateTime.now());
        when(callback.getStatus()).thenReturn(Callback.Status.WAITING.name());

        // when
        Page<CallbackUsageHistoryResponse> result = callbackService.getCallbackHistoryOfGuard(memberId, pageable);

        // then
        assertEquals(1, result.getContent().size());
        assertEquals("SeniorName", result.getContent().getFirst().seniorName());
    }

    @Nested
    @DisplayName("대기 상태인 콜백 조회 테스트")
    class GetWaitingCallbackTest {

        @Test
        @DisplayName("정상적인 시니또가 요청하면 성공적으로 조회가 된다")
        void shouldGetWaitingCallbacks() {
            //given
            Long memberId = 1L;
            Pageable pageable = PageRequest.of(0, 10);
            Member member = mock(Member.class);

            Callback callback = mock(Callback.class);
            when(callback.getId()).thenReturn(1L);
            when(callback.getPostTime()).thenReturn(LocalDateTime.now());
            when(callback.getStatus()).thenReturn(Callback.Status.WAITING.name());
            when(callback.getSeniorName()).thenReturn("James");
            when(callback.getSeniorId()).thenReturn(1L);
            Page<Callback> callbackPage = new PageImpl<>(List.of(callback));

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(true);
            when(callbackRepository.findAllByStatus(Callback.Status.WAITING, pageable)).thenReturn(callbackPage);

            //when
            Page<CallbackResponse> result = callbackService.getWaitingCallbacks(memberId, pageable);

            //then
            assertEquals(1, result.getContent().size());
            assertEquals("James", result.getContent().getFirst().seniorName());
        }

        @Test
        @DisplayName("memberId 가 Member DB에 없는 경우 예외를 발생시켜야 한다")
        void getWaitingCallbacks_Fail_WhenNotMember() {
            //given
            Long memberId = 1L;
            Pageable pageable = PageRequest.of(0, 10);

            when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

            //when then
            assertThrows(NotFoundException.class, () -> callbackService.getWaitingCallbacks(memberId, pageable));
        }

        @Test
        @DisplayName("멤버이더라도 시니또가 아닌경우는 예외를 발생시켜야 한다")
        void getCallbacks_Fail_WhenNotSinitto() {
            //given
            Long memberId = 1L;
            Pageable pageable = PageRequest.of(0, 10);
            Member member = mock(Member.class);

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(false);

            //when then
            assertThrows(ForbiddenException.class, () -> callbackService.getWaitingCallbacks(memberId, pageable));
        }
    }

    @Nested
    @DisplayName("콜백 상태 변화 테스트")
    class CallbackStatusChangeTest {

        @Test
        @DisplayName("시니또가 WAITING 상태인 콜백을 수락할 수 있다. 이를 시니또가 콜백을 할당 받는다고 표현함")
        void acceptCallbackBySinitto() {
            //given
            Long memberId = 1L;
            Long callbackId = 1L;

            Member member = mock(Member.class);
            Callback callback = mock(Callback.class);

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(true);
            when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));

            //when
            callbackService.acceptCallbackBySinitto(memberId, callbackId);

            //then
            verify(callback).assignMember(memberId);
            verify(callback).changeStatusToInProgress();
        }

        @Test
        @DisplayName("WAITING 상태인 콜백을 할당 받은 시니또가 할 일을 다하고, PENDING_COMPLETE 상태로 전환시킨다")
        void changeCallbackStatusToCompleteByGuard() {
            //given
            Long memberId = 1L;
            Long callbackId = 1L;
            Long assignedMemberId = 1L;

            Member member = mock(Member.class);
            Callback callback = mock(Callback.class);

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(true);
            when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
            when(callback.getAssignedMemberId()).thenReturn(assignedMemberId);

            //when
            callbackService.changeCallbackStatusToPendingCompleteBySinitto(memberId, callbackId);

            //then
            verify(callback).changeStatusToPendingComplete();
        }

        @Test
        @DisplayName("시니또가 본인이 수락하여 할당 받은 콜백을 취소할 수 있다")
        void cancelCallbackAssignmentBySinitto() {
            //given
            Long memberId = 1L;
            Long callbackId = 1L;

            Member member = mock(Member.class);
            Callback callback = mock(Callback.class);

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(true);
            when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
            when(callback.getAssignedMemberId()).thenReturn(1L);

            //when
            callbackService.cancelCallbackAssignmentBySinitto(memberId, callbackId);

            //then
            verify(callback).cancelAssignment();
            verify(callback).changeStatusToWaiting();
        }

        @Test
        @DisplayName("보호자가 콜백 대기 상태를 완료 생태로 변경 - 성공")
        void changeCallbackStatusToCompleteByGuardByGuard() {
            //given
            Long memberId = 1L;
            Long callbackId = 1L;
            Member member = mock(Member.class);
            Callback callback = mock(Callback.class);
            Senior senior = mock(Senior.class);

            when(member.getId()).thenReturn(1L);
            when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
            when(callback.getSenior()).thenReturn(senior);
            when(senior.getMember()).thenReturn(member);
            when(senior.getMember().getId()).thenReturn(1L);

            //when
            callbackService.changeCallbackStatusToCompleteByGuard(memberId, callbackId);

            //then
            verify(callback).changeStatusToComplete();
        }

        @Test
        @DisplayName("보호자가 콜백 대기 상태를 완료 생태로 변경 - 일치하는 보호자 ID가 아니어서 실패")
        void changeCallbackStatusToCompleteByGuardByGuard_fail() {
            //given
            Long memberId = 10L;
            Long callbackId = 1L;
            Member member = mock(Member.class);
            Callback callback = mock(Callback.class);
            Senior senior = mock(Senior.class);

            when(member.getId()).thenReturn(1L);
            when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
            when(callback.getSenior()).thenReturn(senior);
            when(senior.getMember()).thenReturn(member);
            when(senior.getMember().getId()).thenReturn(1L);

            //when then
            assertThrows(ForbiddenException.class, () -> callbackService.changeCallbackStatusToCompleteByGuard(memberId, callbackId));
        }
    }

    @Nested
    @DisplayName("시니어가 우리 서비스에 전화를 걸어 새로운 콜백 등록 테스트")
    class CreateNewCallbackTest {

        @Test
        @DisplayName("성공적으로 새로운 콜백 등록되면 감사하다는 인사와 잠시만 기다려 달라는 메시지를 응답한다")
        void createCallbackByCall() {
            //given
            String fromPhoneNumber = "+821012341234";
            String trimmedPhoneNumber = TwilioHelper.trimPhoneNumber(fromPhoneNumber);
            Senior senior = mock(Senior.class);
            Member member = mock(Member.class);

            when(seniorRepository.findByPhoneNumber(trimmedPhoneNumber)).thenReturn(Optional.of(senior));
            when(senior.getMember()).thenReturn(member);
            when(member.getId()).thenReturn(1L);

            //when
            String result = callbackService.createCallbackByCall(fromPhoneNumber);

            //then
            assertEquals(TwilioHelper.convertMessageToTwiML("감사합니다. 잠시만 기다려주세요."), result);
            verify(callbackRepository, times(1)).save(any());
            assertNotNull(result);
        }

        @Test
        @DisplayName("서비스로 걸려온 전화번호가 등록된 번호가 아닐 때, 등록된 사용자가 아니라는 메시지를 응답한다")
        void createCallbackByCallingCallback_fail() {
            //given
            String fromPhoneNumber = "+821012341234";
            String trimmedPhoneNumber = TwilioHelper.trimPhoneNumber(fromPhoneNumber);

            when(seniorRepository.findByPhoneNumber(trimmedPhoneNumber)).thenReturn(Optional.empty());

            //when
            String result = callbackService.createCallbackByCall(fromPhoneNumber);

            //then
            assertEquals(TwilioHelper.convertMessageToTwiML("등록된 사용자가 아닙니다. 서비스 이용이 불가합니다."), result);
            verify(callbackRepository, never()).save(any());
            verify(pointService, never()).deductPoint(anyLong(), anyInt(), any());
            assertNotNull(result);
        }

        @Test
        @DisplayName("새로운 콜백 등록할 때 포인트가 부족할 때, 포인트가 부족하다는 메시지를 응답한다")
        void createCallbackByCallingCallback_fail1() {
            //given
            String fromPhoneNumber = "+821012341234";
            String trimmedPhoneNumber = TwilioHelper.trimPhoneNumber(fromPhoneNumber);
            Member member = mock(Member.class);
            Senior senior = mock(Senior.class);

            when(seniorRepository.findByPhoneNumber(trimmedPhoneNumber)).thenReturn(Optional.of(senior));
            when(senior.getMember()).thenReturn(member);
            when(member.getId()).thenReturn(1L);
            doThrow(new ForbiddenException("포인트 부족")).when(pointService).deductPoint(anyLong(), anyInt(), any(PointLog.Content.class));

            //when
            String result = callbackService.createCallbackByCall(fromPhoneNumber);

            //then
            assertEquals(TwilioHelper.convertMessageToTwiML("포인트가 부족합니다. 서비스 이용이 불가합니다."), result);
            verify(callbackRepository, never()).save(any());
            assertNotNull(result);
        }

        @Test
        @DisplayName("이미 시니어가 신청한 콜백이 대기중이거나 진행중일때, 이미 접수된 콜백이 있다는 메시지를 응답한다")
        void createCallbackByCallingCallback_fail2() {
            //given
            String fromPhoneNumber = "+821012341234";
            String trimmedPhoneNumber = TwilioHelper.trimPhoneNumber(fromPhoneNumber);
            Senior senior = mock(Senior.class);
            Point point = mock(Point.class);

            when(seniorRepository.findByPhoneNumber(trimmedPhoneNumber)).thenReturn(Optional.of(senior));
            when(callbackRepository.existsBySeniorAndStatusIn(any(), any())).thenReturn(true);

            //when
            String result = callbackService.createCallbackByCall(fromPhoneNumber);

            //then
            assertEquals(TwilioHelper.convertMessageToTwiML("어르신의 요청이 이미 접수되었습니다. 잠시 기다려주시면 연락드리겠습니다."), result);
            verify(point, never()).deduct(anyInt());
            verify(callbackRepository, never()).save(any());
            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("시니또에 할당 된 콜백 조회 테스트")
    class GetAssignedCallbackTest {

        @Test
        @DisplayName("시니또에게 할당된 콜백이 있으면 조회가 된다")
        void getAcceptedCallback() {
            //given
            Long memberId = 1L;
            Callback callback = mock(Callback.class);
            Member member = mock(Member.class);

            //when
            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(true);
            when(callback.getId()).thenReturn(10L);

            when(callbackRepository.findByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS)).thenReturn(Optional.of(callback));
            CallbackResponse result = callbackService.getAcceptedCallback(memberId);

            //then
            assertEquals(10L, result.callbackId());
        }

        @Test
        @DisplayName("시니또에게 할당된 콜백이 없으면 예외를 발생 시킨다")
        void getAcceptedCallback_fail() {
            //given
            Long memberId = 1L;
            Member member = mock(Member.class);

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
            when(member.isSinitto()).thenReturn(true);
            when(callbackRepository.findByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS)).thenReturn(Optional.empty());

            //when then
            assertThrows(NotFoundException.class, () -> callbackService.getAcceptedCallback(memberId));
        }

    }

    @Nested
    @DisplayName("일정 기간동안 PENDING_COMPLETE 상태인 콜백 자동으로 COMPLETE 시키는 정책 테스트")
    class CallbackPolicyTest {

        @Test
        @DisplayName("일정 기간동안 PENDING_COMPLETE 인 콜백 자동으로 COMPLETE 로 전환되고 시니또에게 포인트도 적립되야 한다")
        void changeOldPendingCompleteToCompleteByPolicy_Success() {
            // Given
            Callback callback1 = mock(Callback.class);

            when(callback1.getAssignedMemberId()).thenReturn(1L);

            when(callbackRepository.findAllByStatusAndPendingCompleteTimeBetween(eq(Callback.Status.PENDING_COMPLETE), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(List.of(callback1));

            // When
            callbackService.changeOldPendingCompleteToCompleteByPolicy();

            // Then
            verify(callback1, times(1)).changeStatusToComplete();
            verify(pointService, times(1)).earnPoint(anyLong(), anyInt(), any());
        }

        @Test
        @DisplayName("조건에 맞는 콜백이 없는경우 pointService 는 절대 사용이 안된다")
        void changeOldPendingCompleteToCompleteByPolicy_Success_zeroList() {
            // Given
            when(callbackRepository.findAllByStatusAndPendingCompleteTimeBetween(eq(Callback.Status.PENDING_COMPLETE), any(LocalDateTime.class), any(LocalDateTime.class)))
                    .thenReturn(List.of());

            // When
            callbackService.changeOldPendingCompleteToCompleteByPolicy();

            // Then
            verify(pointService, never()).earnPoint(anyLong(), anyInt(), any());
        }

        @Test
        @DisplayName("LocalDateTime 의 isBefore 메서드 공부 겸 테스트, 엣지 케이스")
        void localDateTimeBeforeCalculateTest() {
            //given
            //메서드 실행 시간
            LocalDateTime 일월3일13시00분 = LocalDateTime.of(2024, 1, 3, 13, 0);
            LocalDateTime 일월3일13시10분 = LocalDateTime.of(2024, 1, 3, 13, 10);

            //콜백이 Pending Complete 상태가 된 시간
            LocalDateTime 일월1일12시59분 = LocalDateTime.of(2024, 1, 1, 12, 59);
            LocalDateTime 일월1일13시00분 = LocalDateTime.of(2024, 1, 1, 13, 0);
            LocalDateTime 일월1일13시01분 = LocalDateTime.of(2024, 1, 1, 13, 1);

            //when then
            // 1월 3일 13시 00분에 2일전에 COMPLETE 된 콜백의 존재를 확인한다.
            assertTrue(일월1일12시59분.isBefore(일월3일13시00분.minusDays(2)));
            assertFalse(일월1일13시00분.isBefore(일월3일13시00분.minusDays(2)));
            assertFalse(일월1일13시01분.isBefore(일월3일13시00분.minusDays(2)));

            //10분뒤인 1월 3일 13시 10분에 2일전에 COMPLETE 된 콜백의 존재를 확인한다. 결국 모두 TRUE 로 처리가 되는것을 확인.
            assertTrue(일월1일12시59분.isBefore(일월3일13시10분.minusDays(2)));
            assertTrue(일월1일13시00분.isBefore(일월3일13시10분.minusDays(2)));
            assertTrue(일월1일13시01분.isBefore(일월3일13시10분.minusDays(2)));
        }
    }

    @Nested
    @DisplayName("시니또 전용 콜백 단건 조회")
    class GetCallbackForSinitto {
        @Test
        @DisplayName("시니또용 콜백 단건 조회 - 1.대기상태 아님 2.AssignedMemberId 이 null 아님 + 해당 콜백에 할당 된 시니또 맞음")
        void getCallbackForSinitto() {
            //given
            Long memberId = 1L;
            Long callbackId = 1L;
            Callback callback = mock(Callback.class);
            Senior senior = mock(Senior.class);

            when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));

            when(callback.getStatus()).thenReturn(Callback.Status.IN_PROGRESS.toString());
            when(callback.getAssignedMemberId()).thenReturn(1L);
            when(callback.getId()).thenReturn(1L);
            when(callback.getSeniorName()).thenReturn("SeniorName");
            when(callback.getPostTime()).thenReturn(LocalDateTime.now());
            when(callback.getSeniorId()).thenReturn(1L);
            when(callback.getSenior()).thenReturn(senior);
            when(callback.getSenior().getPhoneNumber()).thenReturn("01012341234");

            //when
            CallbackForSinittoResponse result = callbackService.getCallbackForSinitto(memberId, callbackId);

            //then
            assertTrue(result.isAssignedToSelf());
            assertEquals("01012341234", result.seniorPhoneNumber());
        }

        @Test
        @DisplayName("시니또용 콜백 단건 조회 - 1.대기상태 아님 2.AssignedMemberId 이 null 인 상황은 예외를 던져야한다")
        void getCallbackForSinitto2() {
            //given
            Long memberId = 1L;
            Long callbackId = 1L;
            Callback callback = mock(Callback.class);

            when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
            when(callback.getAssignedMemberId()).thenReturn(null);
            when(callback.getStatus()).thenReturn(Callback.Status.IN_PROGRESS.toString());

            //when then
            assertThrows(ForbiddenException.class, () -> callbackService.getCallbackForSinitto(memberId, callbackId));
        }

        @Test
        @DisplayName("시니또용 콜백 단건 조회 - 1.대기상태 아님 2.AssignedMemberId 이 null 은 아닌데 해당 콜백에 할당된 시니또는 아닌 상황은 예외를 던져야 한다")
        void getCallbackForSinitto3() {
            //given
            Long memberId = 1L;
            Long callbackId = 1L;
            Callback callback = mock(Callback.class);

            when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
            when(callback.getAssignedMemberId()).thenReturn(999L);
            when(callback.getStatus()).thenReturn(Callback.Status.IN_PROGRESS.toString());

            //when then
            assertThrows(ForbiddenException.class, () -> callbackService.getCallbackForSinitto(memberId, callbackId));
        }

        @Test
        @DisplayName("시니또용 콜백 단건 조회 - 1.콜백이 '대기상태' 인 경우(모든 시니또가 조회 가능하다) 시니어 번호가 \"\" 로 전달되야한다")
        void getCallbackForSinitto4() {
            //given
            Long memberId = 1L;
            Long callbackId = 1L;
            Callback callback = mock(Callback.class);

            when(callbackRepository.findById(callbackId)).thenReturn(Optional.of(callback));
            when(callback.getStatus()).thenReturn(Callback.Status.WAITING.toString());

            //when
            CallbackForSinittoResponse result = callbackService.getCallbackForSinitto(memberId, callbackId);

            //then
            assertFalse(result.isAssignedToSelf());
            assertEquals("", result.seniorPhoneNumber());
        }
    }
}
