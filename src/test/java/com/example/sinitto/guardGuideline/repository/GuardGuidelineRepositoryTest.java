package com.example.sinitto.guardGuideline.repository;


import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.guardGuideline.entity.GuardGuideline;
import com.example.sinitto.guardGuideline.entity.GuardGuideline.Type;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GuardGuidelineRepositoryTest {

    @Autowired
    private GuardGuidelineRepository guardGuidelineRepository;

    @Autowired
    private SeniorRepository seniorRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Senior senior;

    @BeforeEach
    void setUp() {
        // 테스트용 Member 생성 및 저장
        Member member = new Member("Test Member", "010-1234-5678", "test@member.com", true);
        memberRepository.save(member);

        // 테스트용 Senior 생성 및 저장
        senior = new Senior("Test Senior", "010-9876-5432", member);
        seniorRepository.save(senior);

        // 테스트용 GuardGuideline 여러 개 생성 및 저장
        GuardGuideline guideline1 = new GuardGuideline(Type.TAXI, "Taxi Guideline", "Content for taxi", senior);
        GuardGuideline guideline2 = new GuardGuideline(Type.DELIVERY, "Delivery Guideline", "Content for delivery", senior);
        guardGuidelineRepository.save(guideline1);
        guardGuidelineRepository.save(guideline2);
    }

    @Test
    void testFindBySeniorId() {
        // Senior ID를 기반으로 GuardGuideline 목록 조회
        List<GuardGuideline> guidelines = guardGuidelineRepository.findBySeniorId(senior.getId());

        assertEquals(2, guidelines.size());
        assertTrue(guidelines.stream().anyMatch(g -> g.getTitle().equals("Taxi Guideline")));
        assertTrue(guidelines.stream().anyMatch(g -> g.getTitle().equals("Delivery Guideline")));
    }

    @Test
    void testFindBySeniorIdAndType() {
        // 특정 Senior ID와 Type에 따른 GuardGuideline 조회
        List<GuardGuideline> taxiGuidelines = guardGuidelineRepository.findBySeniorIdAndType(senior.getId(), Type.TAXI);

        assertEquals(1, taxiGuidelines.size());
        assertEquals("Taxi Guideline", taxiGuidelines.get(0).getTitle());
        assertEquals(Type.TAXI, taxiGuidelines.get(0).getType());
    }

    @Test
    void testFindBySeniorIdAndType_NoResult() {
        // 존재하지 않는 Senior ID와 Type으로 검색했을 때 빈 결과가 나오는지 테스트
        Long nonexistentSeniorId = -1L; // 존재하지 않는 Senior ID
        List<GuardGuideline> guidelines = guardGuidelineRepository.findBySeniorIdAndType(nonexistentSeniorId, Type.DELIVERY);

        // 결과가 없으므로 리스트가 비어 있는지 확인
        assertTrue(guidelines.isEmpty());
    }


    @Test
    void testSaveAndDelete() {
        // 새로운 GuardGuideline 저장
        GuardGuideline guideline = new GuardGuideline(Type.DELIVERY, "New Delivery Guideline", "New content", senior);
        guardGuidelineRepository.save(guideline);

        // 저장된 엔터티 확인
        List<GuardGuideline> guidelines = guardGuidelineRepository.findBySeniorIdAndType(senior.getId(), Type.DELIVERY);
        assertTrue(guidelines.stream().anyMatch(g -> g.getTitle().equals("New Delivery Guideline")));

        // 엔터티 삭제
        guardGuidelineRepository.delete(guideline);

        // 삭제 확인
        List<GuardGuideline> deletedGuidelines = guardGuidelineRepository.findBySeniorIdAndType(senior.getId(), Type.DELIVERY);
        assertFalse(deletedGuidelines.stream().anyMatch(g -> g.getTitle().equals("New Delivery Guideline")));
    }
}
