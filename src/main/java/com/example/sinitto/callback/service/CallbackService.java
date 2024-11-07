package com.example.sinitto.callback.service;

import com.example.sinitto.callback.dto.CallbackForSinittoResponse;
import com.example.sinitto.callback.dto.CallbackResponse;
import com.example.sinitto.callback.dto.CallbackUsageHistoryResponse;
import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.callback.util.TwilioHelper;
import com.example.sinitto.common.exception.ConflictException;
import com.example.sinitto.common.exception.ForbiddenException;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.service.PointService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CallbackService {

    private static final int CALLBACK_PRICE = 1500;
    private static final int DAYS_FOR_AUTO_COMPLETE = 2;
    private static final String SUCCESS_MESSAGE = "감사합니다. 잠시만 기다려주세요.";
    private static final String FAIL_MESSAGE_NOT_ENROLLED = "등록된 사용자가 아닙니다. 서비스 이용이 불가합니다.";
    private static final String FAIL_MESSAGE_NOT_ENOUGH_POINT = "포인트가 부족합니다. 서비스 이용이 불가합니다.";
    private static final String FAIL_MESSAGE_ALREADY_HAS_CALLBACK_IN_PROGRESS_OR_WAITING = "어르신의 요청이 이미 접수되었습니다. 잠시 기다려주시면 연락드리겠습니다.";
    private final CallbackRepository callbackRepository;
    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;
    private final PointService pointService;

    public CallbackService(CallbackRepository callbackRepository, MemberRepository memberRepository, SeniorRepository seniorRepository, PointService pointService) {
        this.callbackRepository = callbackRepository;
        this.memberRepository = memberRepository;
        this.seniorRepository = seniorRepository;
        this.pointService = pointService;
    }

    @Transactional(readOnly = true)
    public Page<CallbackResponse> getWaitingCallbacks(Long memberId, Pageable pageable) {

        checkIsSinitto(memberId);

        return callbackRepository.findAllByStatus(Callback.Status.WAITING, pageable)
                .map((callback) -> new CallbackResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus(), callback.getSeniorId()));
    }

    @Transactional
    public void acceptCallbackBySinitto(Long memberId, Long callbackId) {

        checkIsSinitto(memberId);

        if (callbackRepository.existsByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS)) {
            throw new ConflictException("이 요청을 한 시니또는 이미 진행중인 콜백이 있습니다.");
        }

        Callback callback = getCallbackOrThrow(callbackId);

        callback.assignMember(memberId);
        callback.changeStatusToInProgress();
    }

    @Transactional
    public void changeCallbackStatusToPendingCompleteBySinitto(Long memberId, Long callbackId) {

        checkIsSinitto(memberId);

        Callback callback = getCallbackOrThrow(callbackId);

        checkAssignment(memberId, callback.getAssignedMemberId());

        callback.changeStatusToPendingComplete();
        callback.setPendingCompleteTime(LocalDateTime.now());
    }

    @Transactional
    public void changeCallbackStatusToCompleteByGuard(Long memberId, Long callbackId) {

        Callback callback = getCallbackOrThrow(callbackId);

        Senior senior = callback.getSenior();
        Long guardId = senior.getMember().getId();

        if (!guardId.equals(memberId)) {
            throw new ForbiddenException("이 API 를 요청한 보호자는 이 콜백을 요청 한 시니어의 보호자가 아닙니다.");
        }

        pointService.earnPoint(callback.getAssignedMemberId(), CALLBACK_PRICE, PointLog.Content.COMPLETE_CALLBACK_AND_EARN);
        callback.changeStatusToComplete();
    }

    @Scheduled(cron = "0 */10 * * * *")
    public void changeOldPendingCompleteToCompleteByPolicy() {

        LocalDateTime referenceDateTimeForComplete = LocalDateTime.now().minusDays(DAYS_FOR_AUTO_COMPLETE);

        List<Callback> callbacks = callbackRepository.findAllByStatusAndPendingCompleteTimeBetween(Callback.Status.PENDING_COMPLETE, referenceDateTimeForComplete.minusMinutes(5), referenceDateTimeForComplete.plusMinutes(5));

        for (Callback callback : callbacks) {
            completeCallbackIndividually(callback);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void completeCallbackIndividually(Callback callback) {

        pointService.earnPoint(callback.getAssignedMemberId(), CALLBACK_PRICE, PointLog.Content.COMPLETE_CALLBACK_AND_EARN);
        callback.changeStatusToComplete();
    }

    @Transactional
    public void cancelCallbackAssignmentBySinitto(Long memberId, Long callbackId) {

        checkIsSinitto(memberId);

        Callback callback = getCallbackOrThrow(callbackId);

        checkAssignment(memberId, callback.getAssignedMemberId());

        callback.cancelAssignment();
        callback.changeStatusToWaiting();
    }

    @Transactional
    public String createCallbackByCall(String fromNumber) {

        String phoneNumber = TwilioHelper.trimPhoneNumber(fromNumber);

        Senior senior = seniorRepository.findByPhoneNumber(phoneNumber)
                .orElse(null);

        if (senior == null) {
            return TwilioHelper.convertMessageToTwiML(FAIL_MESSAGE_NOT_ENROLLED);
        }

        if (callbackRepository.existsBySeniorAndStatusIn(senior, List.of(Callback.Status.WAITING, Callback.Status.IN_PROGRESS))) {
            return TwilioHelper.convertMessageToTwiML(FAIL_MESSAGE_ALREADY_HAS_CALLBACK_IN_PROGRESS_OR_WAITING);
        }

        try {
            pointService.deductPoint(senior.getMember().getId(), CALLBACK_PRICE, PointLog.Content.SPEND_COMPLETE_CALLBACK);
        } catch (Exception e) {
            return TwilioHelper.convertMessageToTwiML(FAIL_MESSAGE_NOT_ENOUGH_POINT);
        }

        callbackRepository.save(new Callback(Callback.Status.WAITING, senior));

        return TwilioHelper.convertMessageToTwiML(SUCCESS_MESSAGE);
    }

    @Transactional(readOnly = true)
    public CallbackResponse getAcceptedCallback(Long memberId) {

        checkIsSinitto(memberId);

        Callback callback = callbackRepository.findByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS)
                .orElseThrow(() -> new NotFoundException("요청한 시니또에 할당된 콜백이 없습니다"));

        return new CallbackResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus(), callback.getSeniorId());
    }

    private void checkIsSinitto(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("멤버가 아닙니다"));

        if (!member.isSinitto()) {
            throw new ForbiddenException("시니또가 아닙니다");
        }
    }

    private Callback getCallbackOrThrow(Long callbackId) {

        return callbackRepository.findById(callbackId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 콜백입니다"));
    }

    private void checkAssignment(Long memberId, Long assignedMemberId) {

        if (!assignedMemberId.equals(memberId)) {
            throw new ForbiddenException("이 콜백에 할당된 시니또가 아닙니다");
        }
    }

    @Transactional(readOnly = true)
    public Page<CallbackUsageHistoryResponse> getCallbackHistoryOfGuard(Long memberId, Pageable pageable) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("멤버가 아닙니다"));

        List<Senior> seniors = seniorRepository.findAllByMember(member);

        return callbackRepository.findAllBySeniorIn(seniors, pageable)
                .map(callback -> new CallbackUsageHistoryResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus()));
    }

    @Transactional(readOnly = true)
    public CallbackForSinittoResponse getCallbackForSinitto(Long memberId, Long callbackId) {

        Callback callback = callbackRepository.findById(callbackId)
                .orElseThrow(() -> new NotFoundException("해당 콜백 id에 해당하는 콜백이 없습니다."));

        if (!callback.getStatus().equals(Callback.Status.WAITING.toString())) {
            if (callback.getAssignedMemberId() != null && callback.getAssignedMemberId().equals(memberId)) {
                return new CallbackForSinittoResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus(), callback.getSeniorId(), true, callback.getSenior().getPhoneNumber());
            }
            throw new ForbiddenException("대기중인 콜백이 아닌경우 오직 할당받은 시니또만이 콜백을 조회할 수 있습니다.");
        }

        return new CallbackForSinittoResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus(), callback.getSeniorId(), false, "");
    }

    @Transactional
    public void cancelAssignedCallbackIfInProgress(Member member) {

        Callback callback = callbackRepository.findByAssignedMemberIdAndStatus(member.getId(), Callback.Status.IN_PROGRESS)
                .orElse(null);

        if (callback != null) {
            callback.cancelAssignment();
            callback.changeStatusToWaiting();
        }
    }

}
