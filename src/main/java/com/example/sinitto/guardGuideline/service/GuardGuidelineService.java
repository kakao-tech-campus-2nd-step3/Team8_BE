package com.example.sinitto.guardGuideline.service;


import com.example.sinitto.guard.exception.SeniorNotFoundException;
import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineRequest;
import com.example.sinitto.guardGuideline.dto.GuardGuidelineResponse;
import com.example.sinitto.guardGuideline.entity.GuardGuideline;
import com.example.sinitto.guardGuideline.entity.GuardGuideline.Type;
import com.example.sinitto.guardGuideline.exception.GuardGuidelineNotFoundException;
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

    public GuardGuidelineService (GuardGuidelineRepository guardGuidelineRepository, SeniorRepository seniorRepository, MemberRepository memberRepository){
        this.guardGuidelineRepository = guardGuidelineRepository;
        this.seniorRepository = seniorRepository;
    }

    @Transactional
    public void addGuardGuideline(GuardGuidelineRequest guardguidelineRequest){
        Senior senior = seniorRepository.findById(guardguidelineRequest.seniorId()).orElseThrow(
                () -> new SeniorNotFoundException("시니어를 찾을 수 없습니다.")
        );

        guardGuidelineRepository.save(new GuardGuideline(guardguidelineRequest.type(), guardguidelineRequest.title(), guardguidelineRequest.content(), senior));
    }

    @Transactional(readOnly = true)
    public List<GuardGuidelineResponse> readAllGuardGuidelinesByCategory(Long seniorId, Type type){
        List<GuardGuideline> guardGuidelines = guardGuidelineRepository.findBySeniorIdAndType(seniorId, type);

        return guardGuidelines.stream()
                .map(m -> new GuardGuidelineResponse(m.getType(), m.getTitle(), m.getContent()))
                .toList();
    }

    @Transactional
    public void updateGuardGuideline(Long guidelineId, GuardGuidelineRequest guardGuidelineRequest){
        GuardGuideline guardGuideline = guardGuidelineRepository.findById(guidelineId).orElseThrow(
                ()-> new GuardGuidelineNotFoundException("해당 가이드라인이 존재하지 않습니다.")
        );

        guardGuideline.updateGuardGuideline(guardGuidelineRequest.type(), guardGuidelineRequest.title(), guardGuidelineRequest.content());
    }

    @Transactional(readOnly = true)
    public List<GuardGuidelineResponse> readAllGuardGuidelinesBySenior(Long seniorId){
        List<GuardGuideline> guardGuidelines = guardGuidelineRepository.findBySeniorId(seniorId);

        return guardGuidelines.stream()
                .map(m -> new GuardGuidelineResponse(m.getType(), m.getTitle(), m.getContent()))
                .toList();
    }

    @Transactional(readOnly = true)
    public GuardGuidelineResponse readGuardGuideline(Long guidelineId){
        GuardGuideline guardGuideline = guardGuidelineRepository.findById(guidelineId).orElseThrow(
                ()-> new GuardGuidelineNotFoundException("해당 가이드라인이 존재하지 않습니다.")
        );

        return new GuardGuidelineResponse(guardGuideline.getType(), guardGuideline.getTitle(), guardGuideline.getContent());
    }

}
