package com.example.sinitto.guard.entity;

import com.example.sinitto.guard.dto.SeniorResponse;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SeniorTest {
    private Member member;
    private Senior senior;

    @BeforeEach
    void setup(){
        member = new Member(
                "test",
                "01012345678",
                "test@test.com",
                true
        );
        senior = new Senior("testSenior", "01000000000", member);
    }

    @Test
    @DisplayName("Senior 엔티티 생성자 테스트")
    void counstructorTest(){
        assertThat(senior.getId()).isNull();
        assertThat(senior.getName()).isEqualTo("testSenior");
        assertThat(senior.getPhoneNumber()).isEqualTo("01000000000");
        assertThat(senior.getMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("updateSenior 메소드 테스트")
    void updateSeniorTest(){
        senior.updateSenior("updateSenior", "01011111111");
        assertThat(senior.getName()).isEqualTo("updateSenior");
        assertThat(senior.getPhoneNumber()).isEqualTo("01011111111");
    }

    @Test
    @DisplayName("mapToResponse 메소드 테스트")
    void mapToResponseTest(){
        SeniorResponse response = senior.mapToResponse();
        assertThat(response.seniorId()).isNull();
        assertThat(response.seniorName()).isEqualTo(senior.getName());
        assertThat(response.seniorPhoneNumber()).isEqualTo(senior.getPhoneNumber());
    }
}
