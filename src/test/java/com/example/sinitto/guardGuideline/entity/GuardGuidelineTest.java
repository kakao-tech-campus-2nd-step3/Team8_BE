package com.example.sinitto.guardGuideline.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GuardGuidelineTest {

    private Member member;
    private Senior senior;

    @BeforeEach
    void setUp() {
        // 테스트용 Member와 Senior 객체 생성
        member = new Member("John Doe", "123456789", "john@example.com", true);
        senior = new Senior("Jane Doe", "987654321", member);
    }

    @Test
    @DisplayName("GuardGuideline 객체 생성 테스트")
    void createGuardGuideline() {
        GuardGuideline guideline = new GuardGuideline(GuardGuideline.Type.TAXI, "Taxi Guidelines", "Details about taxi guidelines", senior);

        assertEquals(GuardGuideline.Type.TAXI, guideline.getType());
        assertEquals("Taxi Guidelines", guideline.getTitle());
        assertEquals("Details about taxi guidelines", guideline.getContent());
        assertEquals(senior, guideline.getSenior());
        assertEquals(senior.getMember(), member);  // Senior와 Member의 관계 확인
    }

    @Test
    @DisplayName("GuardGuideline 업데이트 메서드 테스트")
    void updateGuardGuideline() {
        GuardGuideline guideline = new GuardGuideline(GuardGuideline.Type.TAXI, "Old Title", "Old Content", senior);

        guideline.updateGuardGuideline(GuardGuideline.Type.DELIVERY, "New Title", "New Content");

        assertEquals(GuardGuideline.Type.DELIVERY, guideline.getType());
        assertEquals("New Title", guideline.getTitle());
        assertEquals("New Content", guideline.getContent());
    }

    @Test
    @DisplayName("GuardGuideline 기본 생성자 테스트")
    void guardGuidelineDefaultConstructor() {
        GuardGuideline guideline = new GuardGuideline();
        assertNull(guideline.getId());
        assertNull(guideline.getType());
        assertNull(guideline.getTitle());
        assertNull(guideline.getContent());
        assertNull(guideline.getSenior());
    }
}
