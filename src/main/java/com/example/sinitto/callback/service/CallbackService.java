package com.example.sinitto.callback.service;

import com.example.sinitto.callback.dto.CallbackResponse;
import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.exception.*;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.callback.util.TwilioHelper;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CallbackService {

    private static final String SUCCESS_MESSAGE = "감사합니다. 잠시만 기다려주세요.";
    private static final String FAIL_MESSAGE = "등록된 사용자가 아닙니다. 서비스 이용이 불가합니다.";
    private final CallbackRepository callbackRepository;
    private final MemberRepository memberRepository;
    private final SeniorRepository seniorRepository;

    public CallbackService(CallbackRepository callbackRepository, MemberRepository memberRepository, SeniorRepository seniorRepository) {
        this.callbackRepository = callbackRepository;
        this.memberRepository = memberRepository;
        this.seniorRepository = seniorRepository;
    }

    @Transactional(readOnly = true)
    public Page<CallbackResponse> getCallbacks(Long memberId, Pageable pageable) {

        checkAuthorization(memberId);

        return callbackRepository.findAll(pageable)
                .map((callback) -> new CallbackResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus(), callback.getSeniorId()));
    }

    @Transactional
    public void accept(Long memberId, Long callbackId) {

        checkAuthorization(memberId);

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
    }

    @Transactional
    public void complete(Long memberId, Long callbackId) {

        Callback callback = getCallbackOrThrow(callbackId);

        Senior senior = callback.getSenior();
        Long guardId = senior.getMember().getId();

        if (!guardId.equals(memberId)) {
            throw new GuardMismatchException("이 API를 요청한 보호자는 이 콜백을 요청 한 시니어의 보호자가 아닙니다.");
        }

        callback.changeStatusToComplete();
    }

    @Transactional
    public void cancel(Long memberId, Long callbackId) {

        checkAuthorization(memberId);

        Callback callback = getCallbackOrThrow(callbackId);

        checkAssignment(memberId, callback.getAssignedMemberId());

        callback.cancelAssignment();
        callback.changeStatusToWaiting();
    }

    public String add(String fromNumber) {

        String phoneNumber = TwilioHelper.trimPhoneNumber(fromNumber);

        Optional<Senior> seniorOptional = seniorRepository.findByPhoneNumber(phoneNumber);

        if (seniorOptional.isEmpty()) {
            return TwilioHelper.convertMessageToTwiML(FAIL_MESSAGE);
        }

        Senior senior = seniorOptional.get();
        callbackRepository.save(new Callback(Callback.Status.WAITING, senior));

        return TwilioHelper.convertMessageToTwiML(SUCCESS_MESSAGE);
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
}
