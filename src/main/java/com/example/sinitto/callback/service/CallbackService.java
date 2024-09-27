package com.example.sinitto.callback.service;

import com.example.sinitto.callback.dto.CallbackResponse;
import com.example.sinitto.callback.exception.NotSinittoException;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CallbackService {

    private final CallbackRepository callbackRepository;
    private final MemberRepository memberRepository;

    public CallbackService(CallbackRepository callbackRepository, MemberRepository memberRepository) {
        this.callbackRepository = callbackRepository;
        this.memberRepository = memberRepository;
    }

    public Page<CallbackResponse> getCallbacks(Long memberId, Pageable pageable) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("멤버가 아닙니다"));

        if (!member.isSinitto()) {
            throw new NotSinittoException("시니또가 아닙니다");
        }

        return callbackRepository.findAll(pageable)
                .map((callback) -> new CallbackResponse(callback.getId(), callback.getSeniorName(), callback.getPostTime(), callback.getStatus(), callback.getSeniorId()));
    }
}
