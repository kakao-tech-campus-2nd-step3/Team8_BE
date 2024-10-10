package com.example.sinitto.hellocall.repository;

import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.HelloCallTimeLog;
import com.example.sinitto.helloCall.repository.HelloCallRepository;
import com.example.sinitto.helloCall.repository.HelloCallTimeLogRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.entity.Sinitto;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.sinitto.repository.SinittoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HelloCallTimeLogRepositoryTest {

    @Autowired
    private HelloCallTimeLogRepository helloCallTimeLogRepository;

    @Autowired
    private HelloCallRepository helloCallRepository;

    @Autowired
    private SinittoRepository sinittoRepository;

    @Autowired
    private SeniorRepository seniorRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member sinittoMember;
    private Member seniorMember;
    private Sinitto sinitto;
    private Senior senior;
    private HelloCall helloCall;

    @BeforeEach
    void setUp() {
        sinittoMember = new Member("testSinitto", "01011112222", "sinitto@test.com", true);
        memberRepository.save(sinittoMember);

        sinitto = new Sinitto("sinittoBank", "sinittoAccount", sinittoMember);
        sinittoRepository.save(sinitto);

        seniorMember = new Member("testSenior", "01033334444", "senior@test.com", false);
        memberRepository.save(seniorMember);

        senior = new Senior("SeniorName", "01055556666", seniorMember);
        seniorRepository.save(senior);

        helloCall = new HelloCall(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 2), 10000, 60, "Test requirements", senior);
        helloCallRepository.save(helloCall);
    }

    @Test
    @DisplayName("로그 저장 및 Sinitto와 HelloCall ID 기반으로 특정 HelloCallTimeLog 찾기 테스트")
    void findBySinittoAndHelloCallIdTest() {
        HelloCallTimeLog timeLog = new HelloCallTimeLog(helloCall, sinitto, LocalDateTime.of(2024, 1, 1, 10, 0), LocalDateTime.of(2024, 1, 1, 11, 0));
        helloCallTimeLogRepository.save(timeLog);

        Optional<HelloCallTimeLog> foundTimeLog = helloCallTimeLogRepository.findBySinittoAndAndHelloCallId(sinitto, helloCall.getId());

        assertThat(foundTimeLog).isPresent();
        assertThat(foundTimeLog.get().getHelloCall()).isEqualTo(helloCall);
        assertThat(foundTimeLog.get().getSinitto()).isEqualTo(sinitto);
        assertThat(foundTimeLog.get().getStartDateAndTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertThat(foundTimeLog.get().getEndDateAndTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 11, 0));
    }

    @Test
    @DisplayName("HelloCall ID 기반으로 모든 HelloCallTimeLog 리스트 찾기 테스트")
    void findAllByHelloCallIdTest() {
        HelloCallTimeLog timeLog1 = new HelloCallTimeLog(helloCall, sinitto, LocalDateTime.of(2024, 1, 1, 10, 0), LocalDateTime.of(2024, 1, 1, 11, 0));
        HelloCallTimeLog timeLog2 = new HelloCallTimeLog(helloCall, sinitto, LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 1, 13, 0));

        helloCallTimeLogRepository.save(timeLog1);
        helloCallTimeLogRepository.save(timeLog2);

        List<HelloCallTimeLog> timeLogs = helloCallTimeLogRepository.findAllByHelloCallId(helloCall.getId());

        assertThat(timeLogs).hasSize(2);
        assertThat(timeLogs.get(0).getHelloCall()).isEqualTo(helloCall);
        assertThat(timeLogs.get(1).getHelloCall()).isEqualTo(helloCall);
        assertThat(timeLogs.get(0).getStartDateAndTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertThat(timeLogs.get(1).getStartDateAndTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
    }

    @Test
    @DisplayName("Sinitto와 HelloCall 기반으로 가장 최근의 HelloCallTimeLog 찾기 테스트")
    void findTopBySinittoAndHelloCallOrderByStartDateAndTimeDescTest() {
        HelloCallTimeLog timeLog1 = new HelloCallTimeLog(helloCall, sinitto, LocalDateTime.of(2024, 1, 1, 10, 0), LocalDateTime.of(2024, 1, 1, 11, 0));
        HelloCallTimeLog timeLog2 = new HelloCallTimeLog(helloCall, sinitto, LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 1, 13, 0));

        helloCallTimeLogRepository.save(timeLog1);
        helloCallTimeLogRepository.save(timeLog2);

        Optional<HelloCallTimeLog> recentTimeLog = helloCallTimeLogRepository
                .findTopBySinittoAndHelloCallOrderByStartDateAndTimeDesc(sinitto, helloCall);

        assertThat(recentTimeLog).isPresent();
        assertThat(recentTimeLog.get().getHelloCall()).isEqualTo(helloCall);
        assertThat(recentTimeLog.get().getSinitto()).isEqualTo(sinitto);
        assertThat(recentTimeLog.get().getStartDateAndTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 12, 0));
        assertThat(recentTimeLog.get().getEndDateAndTime()).isEqualTo(LocalDateTime.of(2024, 1, 1, 13, 0));
    }
}
