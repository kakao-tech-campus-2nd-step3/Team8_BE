package com.example.sinitto.hellocall.repository;

import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.TimeSlot;
import com.example.sinitto.helloCall.repository.HelloCallRepository;
import com.example.sinitto.helloCall.repository.TimeSlotRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TimeSlotRepositoryTest {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private HelloCallRepository helloCallRepository;

    @Autowired
    private SeniorRepository seniorRepository;

    @Autowired
    private MemberRepository memberRepository;

    private HelloCall helloCall;
    private Senior senior;
    private Member seniorMember;

    @BeforeEach
    void setUp() {
        seniorMember = new Member("testSenior", "01033334444", "senior@test.com", false);
        memberRepository.save(seniorMember);

        senior = new Senior("SeniorName", "01055556666", seniorMember);
        seniorRepository.save(senior);

        helloCall = new HelloCall(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2), 10000, 60, "테스트 요구사항", senior);
        helloCallRepository.save(helloCall);
    }

    @Test
    @DisplayName("HelloCall 및 DayName을 기반으로 TimeSlot 찾기 테스트")
    void findByHelloCallAndDayTest() {
        TimeSlot timeSlot = new TimeSlot("금", LocalTime.of(10, 0), LocalTime.of(11, 0), helloCall);
        timeSlotRepository.save(timeSlot);

        Optional<TimeSlot> foundTimeSlot = timeSlotRepository.findByHelloCallAndDayName(helloCall, "금");

        assertThat(foundTimeSlot).isPresent();
        assertThat(foundTimeSlot.get().getHelloCall()).isEqualTo(helloCall);
        assertThat(foundTimeSlot.get().getDayName()).isEqualTo("금");
    }

    @Test
    @DisplayName("존재하지 않는 DayName으로 TimeSlot 찾기 테스트")
    void findByHelloCallAndNonExistentDayTest() {
        Optional<TimeSlot> foundTimeSlot = timeSlotRepository.findByHelloCallAndDayName(helloCall, "월");

        assertThat(foundTimeSlot).isNotPresent();
    }

    @Test
    @DisplayName("HelloCall 기반으로 모든 TimeSlot 삭제 테스트")
    void deleteAllByHelloCallTest() {
        TimeSlot timeSlot1 = new TimeSlot("월", LocalTime.of(9, 0), LocalTime.of(10, 0), helloCall);
        TimeSlot timeSlot2 = new TimeSlot("화", LocalTime.of(10, 0), LocalTime.of(11, 0), helloCall);
        timeSlotRepository.save(timeSlot1);
        timeSlotRepository.save(timeSlot2);

        assertThat(timeSlotRepository.findAllByHelloCall(helloCall)).hasSize(2);

        timeSlotRepository.deleteAllByHelloCall(helloCall);

        assertThat(timeSlotRepository.findAllByHelloCall(helloCall)).isEmpty();
    }
}
