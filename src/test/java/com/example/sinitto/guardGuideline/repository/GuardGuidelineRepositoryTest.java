package com.example.sinitto.guardGuideline.repository;


import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.guardGuideline.entity.GuardGuideline;
import com.example.sinitto.guardGuideline.entity.GuardGuideline.Type;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        Member member = new Member("Test Member", "010-1234-5678", "test@member.com", true);
        memberRepository.save(member);

        senior = new Senior("Test Senior", "010-9876-5432", member);
        seniorRepository.save(senior);

        GuardGuideline guideline1 = new GuardGuideline(Type.TAXI, "Taxi Guideline", "Content for taxi", senior);
        GuardGuideline guideline2 = new GuardGuideline(Type.DELIVERY, "Delivery Guideline", "Content for delivery", senior);
        guardGuidelineRepository.save(guideline1);
        guardGuidelineRepository.save(guideline2);
    }

    @Test
    @DisplayName("Senior ID로 GuardGuideline 목록 조회 테스트")
    void testFindBySeniorId() {
        List<GuardGuideline> guidelines = guardGuidelineRepository.findBySeniorId(senior.getId());

        assertEquals(2, guidelines.size());
        assertTrue(guidelines.stream().anyMatch(g -> g.getTitle().equals("Taxi Guideline")));
        assertTrue(guidelines.stream().anyMatch(g -> g.getTitle().equals("Delivery Guideline")));
    }

    @Test
    @DisplayName("Senior ID와 Type으로 GuardGuideline 조회 테스트")
    void testFindBySeniorIdAndType() {
        List<GuardGuideline> taxiGuidelines = guardGuidelineRepository.findBySeniorIdAndType(senior.getId(), Type.TAXI);

        assertEquals(1, taxiGuidelines.size());
        assertEquals("Taxi Guideline", taxiGuidelines.get(0).getTitle());
        assertEquals(Type.TAXI, taxiGuidelines.get(0).getType());
    }

    @Test
    @DisplayName("존재하지 않는 Senior ID와 Type으로 GuardGuideline 조회 테스트")
    void testFindBySeniorIdAndType_NoResult() {
        Long nonexistentSeniorId = -1L; // 존재하지 않는 Senior ID
        List<GuardGuideline> guidelines = guardGuidelineRepository.findBySeniorIdAndType(nonexistentSeniorId, Type.DELIVERY);

        assertTrue(guidelines.isEmpty());
    }


    @Test
    @DisplayName("GuardGuideline 저장 및 삭제 테스트")
    void testSaveAndDelete() {
        GuardGuideline guideline = new GuardGuideline(Type.DELIVERY, "New Delivery Guideline", "New content", senior);
        guardGuidelineRepository.save(guideline);

        List<GuardGuideline> guidelines = guardGuidelineRepository.findBySeniorIdAndType(senior.getId(), Type.DELIVERY);
        assertTrue(guidelines.stream().anyMatch(g -> g.getTitle().equals("New Delivery Guideline")));

        guardGuidelineRepository.delete(guideline);

        List<GuardGuideline> deletedGuidelines = guardGuidelineRepository.findBySeniorIdAndType(senior.getId(), Type.DELIVERY);
        assertFalse(deletedGuidelines.stream().anyMatch(g -> g.getTitle().equals("New Delivery Guideline")));
    }
}
