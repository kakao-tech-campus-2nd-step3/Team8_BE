package com.example.sinitto.callback.service;

import com.example.sinitto.callback.dto.CallbackResponse;
import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.exception.NotAssignedException;
import com.example.sinitto.callback.exception.NotSinittoException;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.callback.util.TwilioHelper;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public void complete(Long memberId, Long callbackId) {

        checkAuthorization(memberId);

        Callback callback = getCallbackOrThrow(callbackId);

        checkAssignment(memberId, callback.getAssignedMemberId());

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

    private void checkAuthorization(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버가 아닙니다"));

        if (!member.isSinitto()) {
            throw new NotSinittoException("시니또가 아닙니다");
        }
    }

    private Callback getCallbackOrThrow(Long callbackId) {

        return callbackRepository.findById(callbackId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 콜백입니다"));
    }

    private void checkAssignment(Long memberId, Long assignedMemberId) {

        if (!assignedMemberId.equals(memberId)) {
            throw new NotAssignedException("이 콜백에 할당된 시니또가 아닙니다");
        }
    }
}
