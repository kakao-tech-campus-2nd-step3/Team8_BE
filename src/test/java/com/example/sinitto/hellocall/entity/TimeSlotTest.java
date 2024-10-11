package com.example.sinitto.hellocall.entity;

import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.TimeSlot;
import com.example.sinitto.helloCall.exception.TimeRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TimeSlotTest {

    private HelloCall helloCall;
    private TimeSlot timeSlot;

    @BeforeEach
    void setup() {
        helloCall = new HelloCall(LocalDate.now(), LocalDate.now().plusDays(1), 10000, 30, "Test Requirement", null);

        timeSlot = new TimeSlot("Monday", LocalTime.of(9, 0), LocalTime.of(10, 0), helloCall);
    }

    @Test
    @DisplayName("TimeSlot 생성자 테스트 - 유효한 입력")
    void constructorTest_ValidInput() {
        assertThat(timeSlot.getDayName()).isEqualTo("Monday");
        assertThat(timeSlot.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(timeSlot.getEndTime()).isEqualTo(LocalTime.of(10, 0));
        assertThat(timeSlot.getHelloCall()).isEqualTo(helloCall);
    }

    @Test
    @DisplayName("TimeSlot 생성자 테스트 - 잘못된 시간 순서")
    void constructorTest_InvalidTimeOrder() {
        assertThatThrownBy(() -> new TimeSlot("Monday", LocalTime.of(10, 0), LocalTime.of(9, 0), helloCall))
                .isInstanceOf(TimeRuleException.class)
                .hasMessage("시작시간이 종료시간 이후일 수 없습니다.");
    }

    @Test
    @DisplayName("TimeSlot 시간 업데이트 테스트 - 유효한 시간")
    void updateTimeSlot_ValidTime() {
        LocalTime newStartTime = LocalTime.of(10, 0);
        LocalTime newEndTime = LocalTime.of(11, 0);

        timeSlot.updateTimeSlot(newStartTime, newEndTime);

        assertThat(timeSlot.getStartTime()).isEqualTo(newStartTime);
        assertThat(timeSlot.getEndTime()).isEqualTo(newEndTime);
    }

    @Test
    @DisplayName("TimeSlot 시간 업데이트 테스트 - 잘못된 시간 순서")
    void updateTimeSlot_InvalidTimeOrder() {
        LocalTime newStartTime = LocalTime.of(11, 0);
        LocalTime newEndTime = LocalTime.of(10, 0);

        assertThatThrownBy(() -> timeSlot.updateTimeSlot(newStartTime, newEndTime))
                .isInstanceOf(TimeRuleException.class)
                .hasMessage("시작시간이 종료시간 이후일 수 없습니다.");
    }
}
