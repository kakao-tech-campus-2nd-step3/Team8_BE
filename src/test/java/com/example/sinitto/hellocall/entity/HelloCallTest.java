package com.example.sinitto.hellocall.entity;

import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.exception.InvalidStatusException;
import com.example.sinitto.helloCall.exception.TimeRuleException;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HelloCallTest {

    private HelloCall helloCall;
    private Senior senior;

    @BeforeEach
    void setup() {
        Member member = new Member("보호자", "01033334444", "test@test.coom", false);
        senior = new Senior("시니어", "01011112222", member);
        helloCall = new HelloCall(
                LocalDate.of(2024, 10, 1),
                LocalDate.of(2024, 10, 10),
                10000,
                30,
                "Hello Call Requirement",
                senior
        );
    }

    @Test
    @DisplayName("HelloCall 생성자 테스트")
    void constructorTest() {
        assertThat(helloCall.getId()).isNull();
        assertThat(helloCall.getStartDate()).isEqualTo(LocalDate.of(2024, 10, 1));
        assertThat(helloCall.getEndDate()).isEqualTo(LocalDate.of(2024, 10, 10));
        assertThat(helloCall.getPrice()).isEqualTo(10000);
        assertThat(helloCall.getServiceTime()).isEqualTo(30);
        assertThat(helloCall.getRequirement()).isEqualTo("Hello Call Requirement");
        assertThat(helloCall.getStatus()).isEqualTo(HelloCall.Status.WAITING);
        assertThat(helloCall.getSenior()).isEqualTo(senior);
    }

    @Test
    @DisplayName("시작날짜가 종료날짜 이후일 때 예외 발생 테스트")
    void constructorThrowsTimeRuleException() {
        assertThatThrownBy(() -> new HelloCall(
                LocalDate.of(2024, 10, 11),
                LocalDate.of(2024, 10, 10),
                10000,
                30,
                "Invalid Requirement",
                senior
        )).isInstanceOf(TimeRuleException.class)
                .hasMessage("시작날짜가 종료날짜 이후일 수 없습니다.");
    }

    @Test
    @DisplayName("상태 변경 테스트")
    void changeStatusTest() {
        helloCall.changeStatusToInProgress();
        assertThat(helloCall.getStatus()).isEqualTo(HelloCall.Status.IN_PROGRESS);

        helloCall.changeStatusToPendingComplete();
        assertThat(helloCall.getStatus()).isEqualTo(HelloCall.Status.PENDING_COMPLETE);

        helloCall.changeStatusToComplete();
        assertThat(helloCall.getStatus()).isEqualTo(HelloCall.Status.COMPLETE);
    }

    @Test
    @DisplayName("상태 변경 예외 발생 테스트")
    void changeStatusThrowsInvalidStatusException() {
        helloCall.changeStatusToInProgress();

        assertThatThrownBy(() -> helloCall.changeStatusToInProgress())
                .isInstanceOf(InvalidStatusException.class)
                .hasMessage("안부전화 서비스가 수행 대기중일 때만 진행중 상태로 변경할 수 있습니다. 현재 상태 : " + HelloCall.Status.IN_PROGRESS);

        helloCall.changeStatusToPendingComplete();

        assertThatThrownBy(() -> helloCall.changeStatusToWaiting())
                .isInstanceOf(InvalidStatusException.class)
                .hasMessage("안부전화 서비스가 수행중일 때만 진행중 상태로 변경할 수 있습니다. 현재 상태 : " + HelloCall.Status.PENDING_COMPLETE);
    }

    @Test
    @DisplayName("Update HelloCall 테스트")
    void updateHelloCallTest() {
        helloCall.updateHelloCall(
                LocalDate.of(2024, 10, 2),
                LocalDate.of(2024, 10, 12),
                12000,
                45,
                "Updated Requirement"
        );

        assertThat(helloCall.getStartDate()).isEqualTo(LocalDate.of(2024, 10, 2));
        assertThat(helloCall.getEndDate()).isEqualTo(LocalDate.of(2024, 10, 12));
        assertThat(helloCall.getPrice()).isEqualTo(12000);
        assertThat(helloCall.getServiceTime()).isEqualTo(45);
        assertThat(helloCall.getRequirement()).isEqualTo("Updated Requirement");
    }

    @Test
    @DisplayName("Update HelloCall 예외 발생 테스트")
    void updateHelloCallThrowsInvalidStatusException() {
        helloCall.changeStatusToInProgress();

        assertThatThrownBy(() -> helloCall.updateHelloCall(
                LocalDate.of(2024, 10, 2),
                LocalDate.of(2024, 10, 12),
                12000,
                45,
                "Updated Requirement"
        )).isInstanceOf(InvalidStatusException.class)
                .hasMessage("안부전화 서비스가 수행 대기중일 때만 수정이 가능합니다.");
    }
}
