package com.example.sinitto.guardGuideline.service;


import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.callback.repository.CallbackRepository;
import com.example.sinitto.common.exception.BadRequestException;
import com.example.sinitto.common.exception.NotFoundException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineRequest;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineResponse;
import com.example.sinitto.guardGuideline.entity.GuardGuideline;
import com.example.sinitto.guardGuideline.entity.GuardGuideline.Type;
import com.example.sinitto.guardGuideline.repository.GuardGuidelineRepository;
import com.example.sinitto.member.entity.Senior;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GuardGuidelineService {

    private final GuardGuidelineRepository guardGuidelineRepository;
    private final SeniorRepository seniorRepository;
    private final CallbackRepository callbackRepository;

    public GuardGuidelineService(GuardGuidelineRepository guardGuidelineRepository, SeniorRepository seniorRepository, CallbackRepository callbackRepository) {
        this.guardGuidelineRepository = guardGuidelineRepository;
        this.seniorRepository = seniorRepository;
        this.callbackRepository = callbackRepository;
    }

    @Transactional
    public void addGuardGuideline(Long memberId, GuardGuidelineRequest guardGuidelineRequest) {
        Senior senior = seniorRepository.findById(guardGuidelineRequest.seniorId()).orElseThrow(
                () -> new NotFoundException("시니어를 찾을 수 없습니다.")
        );
        if (senior.isNotGuard(memberId)) {
            throw new BadRequestException("해당 Guard의 Senior가 아닙니다.");
        }

        guardGuidelineRepository.save(new GuardGuideline(guardGuidelineRequest.type(), guardGuidelineRequest.title(), guardGuidelineRequest.content(), senior));
    }

    @Transactional(readOnly = true)
    public List<GuardGuidelineResponse> readAllGuardGuidelinesByCategoryAndSenior(Long memberId, Long seniorId, Type type) {
        List<GuardGuideline> guardGuidelines = guardGuidelineRepository.findBySeniorIdAndType(seniorId, type);
        Senior senior = seniorRepository.findById(seniorId).orElseThrow(
                () -> new NotFoundException("시니어를 찾을 수 없습니다.")
        );
        if (senior.isNotGuard(memberId)) {
            throw new BadRequestException("해당 Guard의 Senior가 아닙니다.");
        }
        return guardGuidelines.stream()
                .map(m -> new GuardGuidelineResponse(m.getId(), m.getType(), m.getTitle(), m.getContent()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GuardGuidelineResponse> readAllGuardGuidelinesByCategoryAndCallback(Long callbackId, Type type) {
        Callback callback = callbackRepository.findById(callbackId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 콜백입니다"));

        if (callback.getStatus() != Callback.Status.WAITING.name()) {
            throw new BadRequestException("해당 콜백은 대기 상태가 아닙니다.");
        }
        ;
        Long seniorId = callback.getSeniorId();
        List<GuardGuideline> guardGuidelines = guardGuidelineRepository.findBySeniorIdAndType(seniorId, type);

        return guardGuidelines.stream()
                .map(m -> new GuardGuidelineResponse(m.getId(), m.getType(), m.getTitle(), m.getContent()))
                .toList();
    }

    @Transactional
    public void updateGuardGuideline(Long memberId, Long guidelineId, GuardGuidelineRequest guardGuidelineRequest) {
        GuardGuideline guardGuideline = guardGuidelineRepository.findById(guidelineId).orElseThrow(
                () -> new NotFoundException("해당 가이드라인이 존재하지 않습니다.")
        );

        Senior senior = seniorRepository.findById(guardGuidelineRequest.seniorId()).orElseThrow(
                () -> new NotFoundException("시니어를 찾을 수 없습니다.")
        );

        if (senior.isNotGuard(memberId)) {
            throw new BadRequestException("해당 Guard의 Senior가 아닙니다.");
        }

        guardGuideline.updateGuardGuideline(guardGuidelineRequest.type(), guardGuidelineRequest.title(), guardGuidelineRequest.content());
    }

    @Transactional
    public void deleteGuardGuideline(Long memberId, Long guidelineId) {
        GuardGuideline guardGuideline = guardGuidelineRepository.findById(guidelineId).orElseThrow(
                () -> new NotFoundException("해당 가이드라인이 존재하지 않습니다.")
        );
        if (guardGuideline.getSenior().isNotGuard(memberId)) {
            throw new BadRequestException("해당 Guard의 Senior가 아닙니다.");
        }
        guardGuidelineRepository.delete(guardGuideline);
    }

    @Transactional(readOnly = true)
    public List<GuardGuidelineResponse> readAllGuardGuidelinesBySenior(Long memberId, Long seniorId) {
        Senior senior = seniorRepository.findById(seniorId).orElseThrow(
                () -> new NotFoundException("시니어를 찾을 수 없습니다.")
        );
        if (senior.isNotGuard(memberId)) {
            throw new BadRequestException("해당 Guard의 Senior가 아닙니다.");
        }
        List<GuardGuideline> guardGuidelines = guardGuidelineRepository.findBySeniorId(seniorId);
        return guardGuidelines.stream()
                .map(m -> new GuardGuidelineResponse(m.getId(), m.getType(), m.getTitle(), m.getContent()))
                .toList();
    }
}
