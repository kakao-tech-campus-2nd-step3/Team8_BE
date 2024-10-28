package com.example.sinitto.hellocall.entity;

import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.HelloCallTimeLog;
import com.example.sinitto.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class HelloCallTimeLogTest {

    private HelloCall helloCall;
    private Member member;
    private HelloCallTimeLog helloCallTimeLog;

    @BeforeEach
    void setup() {
        helloCall = new HelloCall(LocalDateTime.now().toLocalDate(), LocalDateTime.now().toLocalDate(), 10000, 30, "Test Requirement", null);
        member = new Member("SinittoBankInfo Test", "01012345678", "sinittoBankInfo@test.com", true);

        helloCallTimeLog = new HelloCallTimeLog(helloCall, member);
    }

    @Test
    @DisplayName("HelloCallTimeLog 생성자 테스트")
    void constructorTest() {
        assertThat(helloCallTimeLog).isNotNull();
        assertThat(helloCallTimeLog.getHelloCall()).isEqualTo(helloCall);
        assertThat(helloCallTimeLog.getSinitto()).isEqualTo(member);
    }

    @Test
    @DisplayName("시작 및 종료 시간 설정 테스트")
    void setStartAndEndDateTimeTest() {
        LocalDateTime startDateAndTime = LocalDateTime.now();
        LocalDateTime endDateAndTime = startDateAndTime.plusHours(1);

        helloCallTimeLog.setStartDateAndTime(startDateAndTime);
        helloCallTimeLog.setEndDateAndTime(endDateAndTime);

        assertThat(helloCallTimeLog.getStartDateAndTime()).isEqualTo(startDateAndTime);
        assertThat(helloCallTimeLog.getEndDateAndTime()).isEqualTo(endDateAndTime);
    }

    @Test
    @DisplayName("member 이름 가져오기 테스트")
    void getSinittoNameTest() {
        String expectedName = member.getName();
        assertThat(helloCallTimeLog.getSinittoName()).isEqualTo(expectedName);
    }
}
