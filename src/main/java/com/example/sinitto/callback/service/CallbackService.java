package com.example.sinitto.callback.service;

import com.example.sinitto.callback.dto.CallbackResponse;
import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.exception.NotAssignedException;
import com.example.sinitto.callback.exception.NotSinittoException;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CallbackService {

    private final CallbackRepository callbackRepository;
    private final MemberRepository memberRepository;

    public CallbackService(CallbackRepository callbackRepository, MemberRepository memberRepository) {
        this.callbackRepository = callbackRepository;
        this.memberRepository = memberRepository;
    }

    public Page<CallbackResponse> getCallbacks(Long memberId, Pageable pageable) {

        checkAuthorization(memberId);

        return callbackRepository.findAll(pageable)
                .map((callback) -> new CallbackResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus(), callback.getSeniorId()));
    }

    @Transactional
    public void accept(Long memberId, Long callbackId) {

        checkAuthorization(memberId);
        Callback callback = getCallbackOrThrow(callbackId);
        checkAssignment(memberId, callback.getAssignedMemberId());

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
        callback.changeStatusToInProgress();
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
