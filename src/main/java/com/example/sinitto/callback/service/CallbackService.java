package com.example.sinitto.callback.service;

import com.example.sinitto.callback.dto.CallbackResponse;
import com.example.sinitto.callback.dto.CallbackUsageHistoryResponse;
import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.exception.*;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.callback.util.TwilioHelper;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.point.entity.Point;
import com.example.sinitto.point.entity.PointLog;
import com.example.sinitto.point.exception.PointNotFoundException;
import com.example.sinitto.point.repository.PointLogRepository;
import com.example.sinitto.point.repository.PointRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
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
    private final CallbackRepository callbackRepository;
    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;
    private final PointRepository pointRepository;
    private final PointLogRepository pointLogRepository;

    public CallbackService(CallbackRepository callbackRepository, MemberRepository memberRepository, SeniorRepository seniorRepository, PointRepository pointRepository, PointLogRepository pointLogRepository) {
        this.callbackRepository = callbackRepository;
        this.memberRepository = memberRepository;
        this.seniorRepository = seniorRepository;
        this.pointRepository = pointRepository;
        this.pointLogRepository = pointLogRepository;
    }

    @Transactional(readOnly = true)
    public Page<CallbackResponse> getWaitingCallbacks(Long memberId, Pageable pageable) {

        checkAuthorization(memberId);

        return callbackRepository.findAllByStatus(Callback.Status.WAITING, pageable)
                .map((callback) -> new CallbackResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus(), callback.getSeniorId()));
    }

    @Transactional
    public void accept(Long memberId, Long callbackId) {

        checkAuthorization(memberId);

        if (callbackRepository.existsByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS)) {
            throw new MemberHasInProgressCallbackException("이 요청을 한 시니또는 이미 진행중인 콜백이 있습니다.");
        }

        Callback callback = getCallbackOrThrow(callbackId);

        callback.assignMember(memberId);
        callback.changeStatusToInProgress();
    }

    @Transactional
    public void pendingComplete(Long memberId, Long callbackId) {

        checkAuthorization(memberId);

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
            throw new GuardMismatchException("이 API를 요청한 보호자는 이 콜백을 요청 한 시니어의 보호자가 아닙니다.");
        }

        earnPointForSinitto(callback.getAssignedMemberId());
        callback.changeStatusToComplete();
    }

    private void earnPointForSinitto(Long sinittoMemberId) {

        Point sinittoPoint = pointRepository.findByMemberId(sinittoMemberId)
                .orElseThrow(() -> new PointNotFoundException("포인트 적립 받을 시니또와 연관된 포인트가 없습니다"));

        sinittoPoint.earn(CALLBACK_PRICE);

        pointLogRepository.save(new PointLog(PointLog.Content.COMPLETE_CALLBACK_AND_EARN.getMessage(), sinittoPoint.getMember(), CALLBACK_PRICE, PointLog.Status.EARN));
    }

    @Scheduled(cron = "0 */10 * * * *")
    @Transactional
    public void changeOldPendingCompleteToCompleteByPolicy() {

        LocalDateTime referenceDateTimeForComplete = LocalDateTime.now().minusDays(DAYS_FOR_AUTO_COMPLETE);

        List<Callback> callbacks = callbackRepository.findAllByStatusAndPendingCompleteTimeBefore(Callback.Status.PENDING_COMPLETE, referenceDateTimeForComplete);

        for (Callback callback : callbacks) {

            earnPointForSinitto(callback.getAssignedMemberId());
            callback.changeStatusToComplete();
        }
    }

    @Transactional
    public void cancel(Long memberId, Long callbackId) {

        checkAuthorization(memberId);

        Callback callback = getCallbackOrThrow(callbackId);

        checkAssignment(memberId, callback.getAssignedMemberId());

        callback.cancelAssignment();
        callback.changeStatusToWaiting();
    }

    @Transactional
    public String add(String fromNumber) {

        String phoneNumber = TwilioHelper.trimPhoneNumber(fromNumber);

        Senior senior = findSeniorByPhoneNumber(phoneNumber);
        if (senior == null) {
            return TwilioHelper.convertMessageToTwiML(FAIL_MESSAGE_NOT_ENROLLED);
        }

        Point point = findPointWithWriteLock(senior.getMember().getId());
        if (point == null || !point.isSufficientForDeduction(CALLBACK_PRICE)) {
            return TwilioHelper.convertMessageToTwiML(FAIL_MESSAGE_NOT_ENOUGH_POINT);
        }

        point.deduct(CALLBACK_PRICE);

        pointLogRepository.save(
                new PointLog(
                        PointLog.Content.SPEND_COMPLETE_CALLBACK.getMessage(),
                        senior.getMember(),
                        CALLBACK_PRICE,
                        PointLog.Status.SPEND_COMPLETE)
        );
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior));

        return TwilioHelper.convertMessageToTwiML(SUCCESS_MESSAGE);
    }

    private Senior findSeniorByPhoneNumber(String phoneNumber) {
        return seniorRepository.findByPhoneNumber(phoneNumber)
                .orElse(null);
    }

    private Point findPointWithWriteLock(Long memberId) {
        return pointRepository.findByMemberIdWithWriteLock(memberId)
                .orElse(null);
    }

    public CallbackResponse getAcceptedCallback(Long memberId) {

        checkAuthorization(memberId);

        Callback callback = callbackRepository.findByAssignedMemberIdAndStatus(memberId, Callback.Status.IN_PROGRESS)
                .orElseThrow(() -> new NotExistCallbackException("요청한 시니또에 할당된 콜백이 없습니다"));

        return new CallbackResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus(), callback.getSeniorId());
    }

    private void checkAuthorization(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotMemberException("멤버가 아닙니다"));

        if (!member.isSinitto()) {
            throw new NotSinittoException("시니또가 아닙니다");
        }
    }

    private Callback getCallbackOrThrow(Long callbackId) {

        return callbackRepository.findById(callbackId)
                .orElseThrow(() -> new NotExistCallbackException("존재하지 않는 콜백입니다"));
    }

    private void checkAssignment(Long memberId, Long assignedMemberId) {

        if (!assignedMemberId.equals(memberId)) {
            throw new NotAssignedException("이 콜백에 할당된 시니또가 아닙니다");
        }
    }

    @Transactional(readOnly = true)
    public Page<CallbackUsageHistoryResponse> getCallbackHistoryOfGuard(Long memberId, Pageable pageable) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotMemberException("멤버가 아닙니다"));

        List<Senior> seniors = seniorRepository.findAllByMember(member);


        return callbackRepository.findAllBySeniorIn(seniors, pageable)
                .map(callback -> new CallbackUsageHistoryResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus()));
    }
}
