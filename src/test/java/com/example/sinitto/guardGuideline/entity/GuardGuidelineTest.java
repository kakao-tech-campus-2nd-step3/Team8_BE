package com.example.sinitto.guardGuideline.entity;

import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.guardGuideline.entity.GuardGuideline.Type;
import com.example.sinitto.guardGuideline.repository.GuardGuidelineRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class GuardGuidelineTest {

    @Autowired
    private GuardGuidelineRepository guardGuidelineRepository;

    @Autowired
    private SeniorRepository seniorRepository;

    @Autowired
    private MemberRepository memberRepository;

    // EntityManager 주입
    @PersistenceContext
    private EntityManager entityManager;

    private Senior senior;


    @BeforeEach
    void setUp() {
        // 테스트용 Member 생성 및 저장
        Member member = new Member("Test Member", "010-1234-5678", "test@member.com", true);
        memberRepository.save(member);

        // 테스트용 Senior 생성 및 저장
        senior = new Senior("Test Senior", "010-9876-5432", member);
        seniorRepository.save(senior);
    }

    @Test
    @Rollback(false)
    void testCreateGuardGuideline() {
        // GuardGuideline 생성
        GuardGuideline guideline = new GuardGuideline(Type.TAXI, "Taxi Guideline", "Content about taxi.", senior);
        GuardGuideline savedGuideline = guardGuidelineRepository.save(guideline);

        // 저장된 엔터티 확인
        Optional<GuardGuideline> foundGuideline = guardGuidelineRepository.findById(savedGuideline.getId());
        assertTrue(foundGuideline.isPresent());
        assertEquals("Taxi Guideline", foundGuideline.get().getTitle());
        assertEquals("Content about taxi.", foundGuideline.get().getContent());
        assertEquals(Type.TAXI, foundGuideline.get().getType());
        assertEquals(senior, foundGuideline.get().getSenior());
    }

    @Test
    void testUpdateGuardGuideline() {
        // GuardGuideline 생성 후 저장
        GuardGuideline guideline = new GuardGuideline(Type.DELIVERY, "Delivery Guideline", "Content about delivery.", senior);
        guardGuidelineRepository.save(guideline);

        // 업데이트 실행
        guideline.updateGuardGuideline(Type.TAXI, "Updated Taxi Guideline", "Updated content about taxi.");
        guardGuidelineRepository.save(guideline);

        // 업데이트된 값 검증
        Optional<GuardGuideline> updatedGuideline = guardGuidelineRepository.findById(guideline.getId());
        assertTrue(updatedGuideline.isPresent());
        assertEquals("Updated Taxi Guideline", updatedGuideline.get().getTitle());
        assertEquals("Updated content about taxi.", updatedGuideline.get().getContent());
        assertEquals(Type.TAXI, updatedGuideline.get().getType());
    }

    @Test
    void testDeleteGuardGuideline() {
        // GuardGuideline 생성 후 저장
        GuardGuideline guideline = new GuardGuideline(Type.DELIVERY, "Delivery Guideline", "Content about delivery.", senior);
        guardGuidelineRepository.save(guideline);

        // 엔터티 삭제
        guardGuidelineRepository.delete(guideline);

        // 삭제된 엔터티 검증
        Optional<GuardGuideline> deletedGuideline = guardGuidelineRepository.findById(guideline.getId());
        assertFalse(deletedGuideline.isPresent());
    }

    @Test
    void testCascadeDeleteWithSenior() {
        // GuardGuideline 생성 후 Senior에 연결
        GuardGuideline guideline = new GuardGuideline(Type.TAXI, "Taxi", "Taxi content", senior);
        guardGuidelineRepository.save(guideline);

        // Senior 삭제
        seniorRepository.delete(senior);

        // 세션 동기화 및 캐시 초기화
        entityManager.flush();
        entityManager.clear();

        // GuardGuideline도 함께 삭제됐는지 확인
        Optional<GuardGuideline> deletedGuideline = guardGuidelineRepository.findById(guideline.getId());
        assertFalse(deletedGuideline.isPresent());
    }

}
