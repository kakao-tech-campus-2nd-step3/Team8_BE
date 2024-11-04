package com.example.sinitto.member.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MemberTest {

    private Member member;

    @BeforeEach
    void setup() {
        member = new Member(
                "test",
                "01012345678",
                "test@test.com",
                true
        );
    }

    @Test
    @DisplayName("Member 생성자 테스트")
    void constructorTest() {
        assertThat(member.getId()).isNull();
        assertThat(member.getName()).isEqualTo("test");
        assertThat(member.getPhoneNumber()).isEqualTo("01012345678");
        assertThat(member.getEmail()).isEqualTo("test@test.com");
        assertThat(member.isSinitto()).isTrue();
    }

    @Test
    @DisplayName("Member 정보 업데이트 테스트")
    void updateMemberTest() {
        member.updateMember("updatedName", "01087654321");

        assertThat(member.getName()).isEqualTo("updatedName");
        assertThat(member.getPhoneNumber()).isEqualTo("01087654321");
        assertThat(member.getEmail()).isEqualTo("test@test.com");
    }

    @Test
    @DisplayName("Member SinittoBankInfo 여부 확인 테스트")
    void isSinittoTest() {
        assertThat(member.isSinitto()).isTrue();

        Member anotherMember = new Member(
                "test2",
                "01098765432",
                "test2@test.com",
                false
        );

        assertThat(anotherMember.isSinitto()).isFalse();
    }

}
