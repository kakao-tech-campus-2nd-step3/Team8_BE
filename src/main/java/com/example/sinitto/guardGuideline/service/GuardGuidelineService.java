package com.example.sinitto.guardGuideline.service;


import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.exception.NotExistCallbackException;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.guard.exception.SeniorNotFoundException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineRequest;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineResponse;
import com.example.sinitto.guardGuideline.entity.GuardGuideline;
import com.example.sinitto.guardGuideline.entity.GuardGuideline.Type;
import com.example.sinitto.guardGuideline.exception.GuardGuidelineNotFoundException;
import com.example.sinitto.guardGuideline.exception.SeniorAndGuardGuidelineMismatchException;
import com.example.sinitto.guardGuideline.exception.SeniorAndGuardMemberMismatchException;
import com.example.sinitto.guardGuideline.repository.GuardGuidelineRepository;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuardGuidelineService {

    private final GuardGuidelineRepository guardGuidelineRepository;
    private final SeniorRepository seniorRepository;
    private final CallbackRepository callbackRepository;
    private final MemberRepository memberRepository;

    public GuardGuidelineService(GuardGuidelineRepository guardGuidelineRepository, SeniorRepository seniorRepository, CallbackRepository callbackRepository, MemberRepository memberRepository) {
        this.guardGuidelineRepository = guardGuidelineRepository;
        this.seniorRepository = seniorRepository;
        this.callbackRepository = callbackRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void addGuardGuideline(Long memberId, GuardGuidelineRequest guardGuidelineRequest) {
        Senior senior = seniorRepository.findById(guardGuidelineRequest.seniorId()).orElseThrow(
                () -> new SeniorNotFoundException("시니어를 찾을 수 없습니다.")
        );
        if (senior.isNotGuard(memberId)) {
            throw new SeniorAndGuardMemberMismatchException("해당 Guard의 Senior가 아닙니다.");
        }

        guardGuidelineRepository.save(new GuardGuideline(guardGuidelineRequest.type(), guardGuidelineRequest.title(), guardGuidelineRequest.content(), senior));
    }

    @Transactional(readOnly = true)
    public List<GuardGuidelineResponse> readAllGuardGuidelinesByCategory(Long seniorId, Type type) {
        List<GuardGuideline> guardGuidelines = guardGuidelineRepository.findBySeniorIdAndType(seniorId, type);

        return guardGuidelines.stream()
                .map(m -> new GuardGuidelineResponse(m.getType(), m.getTitle(), m.getContent()))
                .toList();
    }

    @Transactional
    public void updateGuardGuideline(Long memberId, Long guidelineId, GuardGuidelineRequest guardGuidelineRequest) {
        GuardGuideline guardGuideline = guardGuidelineRepository.findById(guidelineId).orElseThrow(
                () -> new GuardGuidelineNotFoundException("해당 가이드라인이 존재하지 않습니다.")
        );

        Senior senior = seniorRepository.findById(guardGuidelineRequest.seniorId()).orElseThrow(
                () -> new SeniorNotFoundException("시니어를 찾을 수 없습니다.")
        );

        if (senior.isNotGuard(memberId)) {
            throw new SeniorAndGuardMemberMismatchException("해당 Guard의 Senior가 아닙니다.");
        }

        guardGuideline.updateGuardGuideline(guardGuidelineRequest.type(), guardGuidelineRequest.title(), guardGuidelineRequest.content());
    }

    @Transactional(readOnly = true)
    public List<GuardGuidelineResponse> readAllGuardGuidelinesBySenior(Long memberId, Long seniorId) {
        Senior senior = seniorRepository.findById(seniorId).orElseThrow(
                () -> new SeniorNotFoundException("시니어를 찾을 수 없습니다.")
        );
        if (senior.isNotGuard(memberId)) {
            throw new SeniorAndGuardMemberMismatchException("해당 Guard의 Senior가 아닙니다.");
        }
        List<GuardGuideline> guardGuidelines = guardGuidelineRepository.findBySeniorId(seniorId);
        return guardGuidelines.stream()
                .map(m -> new GuardGuidelineResponse(m.getType(), m.getTitle(), m.getContent()))
                .toList();
    }

    @Transactional(readOnly = true)
    public GuardGuidelineResponse readGuardGuideline(Long callbackId, Long guidelineId) {

        Callback callback = callbackRepository.findById(callbackId)
                .orElseThrow(() -> new NotExistCallbackException("존재하지 않는 콜백입니다"));

        GuardGuideline guardGuideline = guardGuidelineRepository.findById(guidelineId).orElseThrow(
                () -> new GuardGuidelineNotFoundException("해당 가이드라인이 존재하지 않습니다."));

        if (!callback.getSenior().equals(guardGuideline.getSenior())) {
            throw new SeniorAndGuardGuidelineMismatchException("해당 Senior의 가이드라인이 아닙니다.");
        }

        return new GuardGuidelineResponse(guardGuideline.getType(), guardGuideline.getTitle(), guardGuideline.getContent());
    }
}
