package com.example.sinitto.hellocall.repository;

import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.repository.HelloCallRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class HelloCallRepositoryTest {

    @Autowired
    private HelloCallRepository helloCallRepository;

    @Autowired
    private SeniorRepository seniorRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SinittoRepository sinittoRepository;

    private Member sinittoMember1;
    private Member sinittoMember2;
    private Member seniorMember;
    private Sinitto sinitto1;
    private Sinitto sinitto2;
    private Senior senior;

    @BeforeEach
    void setUp() {
        sinittoMember1 = new Member("test1", "01011111111", "test1@test.com", true);
        memberRepository.save(sinittoMember1);

        sinitto1 = new Sinitto("sinittoBank1", "BankAccount1", sinittoMember1);
        sinittoRepository.save(sinitto1);

        sinittoMember2 = new Member("test2", "01022222222", "test2@test.com", true);
        memberRepository.save(sinittoMember2);

        sinitto2 = new Sinitto("SinittoBank2", "BankAccount2", sinittoMember2);
        sinittoRepository.save(sinitto2);

        seniorMember = new Member("test3", "01033333333", "test3@test.com", false);
        memberRepository.save(seniorMember);

        senior = new Senior("SeniorName", "01044444444", seniorMember);
        seniorRepository.save(senior);
    }

    @Test
    @DisplayName("Senior 기반으로 HelloCall 찾기 테스트")
    void findBySeniorTest() {
        HelloCall helloCall = new HelloCall(LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2), 10000, 60,
                "요구사항", senior);
        helloCallRepository.save(helloCall);

        Optional<HelloCall> foundHelloCall = helloCallRepository.findBySenior(senior);

        assertThat(foundHelloCall).isPresent();
        assertThat(foundHelloCall.get().getSenior()).isEqualTo(senior);
    }

    @Test
    @DisplayName("Sinitto 기반으로 HelloCall 리스트 찾기 테스트")
    void findAllBySinittoTest() {
        Senior senior1 = new Senior("Senior1", "01012345678", sinittoMember2);
        seniorRepository.save(senior1);
        Senior senior2 = new Senior("Senior2", "01098765432", sinittoMember2);
        seniorRepository.save(senior2);

        HelloCall helloCall1 = new HelloCall(LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2), 10000, 60,
                "요구사항1", senior1);
        HelloCall helloCall2 = new HelloCall(LocalDate.of(2024, 1, 3),
                LocalDate.of(2024, 1, 4), 15000, 90,
                "요구사항2", senior2);

        helloCall1.setSinitto(sinitto1);
        helloCall2.setSinitto(sinitto1);

        helloCallRepository.save(helloCall1);
        helloCallRepository.save(helloCall2);

        List<HelloCall> helloCalls = helloCallRepository.findAllBySinitto(sinitto1);

        assertThat(helloCalls).hasSize(2);
        assertThat(helloCalls.get(0).getSinitto()).isEqualTo(sinitto1);
        assertThat(helloCalls.get(1).getSinitto()).isEqualTo(sinitto1);
    }

    @Test
    @DisplayName("Senior 존재 여부 테스트")
    void existsBySeniorTest() {
        HelloCall helloCall = new HelloCall(LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2), 10000, 60,
                "요구사항", senior);
        helloCallRepository.save(helloCall);

        boolean exists = helloCallRepository.existsBySenior(senior);

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Senior가 안부전화에 할당되지 않았을 경우 테스트")
    void doesNotExistBySeniorTest() {
        Senior notExistingSenior = new Senior("NonExistent", "01099999999", seniorMember);
        seniorRepository.save(notExistingSenior);

        boolean exists = helloCallRepository.existsBySenior(notExistingSenior);

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("안부전화 담당 Sinitto가 변경되었을 경우, 변경이 잘 적용되는지 테스트")
    void testChangeSinittoInHelloCall() {
        HelloCall helloCall = new HelloCall(LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2), 10000, 60,
                "요구사항", senior);

        helloCall.setSinitto(sinitto1);
        helloCallRepository.save(helloCall);

        Optional<HelloCall> savedHelloCall = helloCallRepository.findById(helloCall.getId());
        assertThat(savedHelloCall).isPresent();
        assertThat(savedHelloCall.get().getSinitto()).isEqualTo(sinitto1);

        savedHelloCall.get().setSinitto(sinitto2);
        helloCallRepository.save(savedHelloCall.get());

        Optional<HelloCall> updatedHelloCall = helloCallRepository.findById(helloCall.getId());
        assertThat(updatedHelloCall).isPresent();
        assertThat(updatedHelloCall.get().getSinitto()).isEqualTo(sinitto2);
    }
}