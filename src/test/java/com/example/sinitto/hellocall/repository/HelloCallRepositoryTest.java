package com.example.sinitto.hellocall.repository;

import com.example.sinitto.guard.repository.SeniorRepository;
import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.repository.HelloCallRepository;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.sinitto.entity.SinittoBankInfo;
import com.example.sinitto.sinitto.repository.SinittoBankInfoRepository;
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
    private SinittoBankInfoRepository sinittoBankInfoRepository;

    private Member sinittoMember1;
    private Member sinittoMember2;
    private Member seniorMember;
    private SinittoBankInfo sinittoBankInfo1;
    private SinittoBankInfo sinittoBankInfo2;
    private Senior senior;

    @BeforeEach
    void setUp() {
        sinittoMember1 = new Member("test1", "01011111111", "test1@test.com", true);
        memberRepository.save(sinittoMember1);

        sinittoBankInfo1 = new SinittoBankInfo("sinittoBank1", "BankAccount1", sinittoMember1);
        sinittoBankInfoRepository.save(sinittoBankInfo1);

        sinittoMember2 = new Member("test2", "01022222222", "test2@test.com", true);
        memberRepository.save(sinittoMember2);

        sinittoBankInfo2 = new SinittoBankInfo("SinittoBank2", "BankAccount2", sinittoMember2);
        sinittoBankInfoRepository.save(sinittoBankInfo2);

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
    @DisplayName("member 기반으로 HelloCall 리스트 찾기 테스트")
    void findAllByMemberTest() {
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

        helloCall1.setMember(sinittoMember1);
        helloCall2.setMember(sinittoMember1);

        helloCallRepository.save(helloCall1);
        helloCallRepository.save(helloCall2);

        List<HelloCall> helloCalls = helloCallRepository.findAllByMember(sinittoMember1);

        assertThat(helloCalls).hasSize(2);
        assertThat(helloCalls.get(0).getMember()).isEqualTo(sinittoMember1);
        assertThat(helloCalls.get(1).getMember()).isEqualTo(sinittoMember1);
    }

    @Test
    @DisplayName("Senior 존재 여부 테스트")
    void existsBySeniorTest() {
        HelloCall helloCall = new HelloCall(LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2), 10000, 60,
                "요구사항", senior);
        helloCallRepository.save(helloCall);

        boolean exists = helloCallRepository.existsBySeniorAndStatusIn(senior, List.of(HelloCall.Status.WAITING));

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Senior가 안부전화에 할당되지 않았을 경우 테스트")
    void doesNotExistBySeniorTest() {
        Senior notExistingSenior = new Senior("NonExistent", "01099999999", seniorMember);
        seniorRepository.save(notExistingSenior);

        boolean exists = helloCallRepository.existsBySeniorAndStatusIn(notExistingSenior, List.of(HelloCall.Status.WAITING));

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("안부전화 담당 Sinitto가 변경되었을 경우, 변경이 잘 적용되는지 테스트")
    void testChangeSinittoInHelloCall() {
        HelloCall helloCall = new HelloCall(LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2), 10000, 60,
                "요구사항", senior);

        helloCall.setMember(sinittoMember1);
        helloCallRepository.save(helloCall);

        Optional<HelloCall> savedHelloCall = helloCallRepository.findById(helloCall.getId());
        assertThat(savedHelloCall).isPresent();
        assertThat(savedHelloCall.get().getMember()).isEqualTo(sinittoMember1);

        savedHelloCall.get().setMember(sinittoMember2);
        helloCallRepository.save(savedHelloCall.get());

        Optional<HelloCall> updatedHelloCall = helloCallRepository.findById(helloCall.getId());
        assertThat(updatedHelloCall).isPresent();
        assertThat(updatedHelloCall.get().getMember()).isEqualTo(sinittoMember2);
    }

    @Test
    @DisplayName("Senior 리스트 기반으로 HelloCall 찾기 테스트")
    void findAllBySeniorInTest() {
        Senior senior1 = new Senior("Senior1", "01055555555", seniorMember);
        Senior senior2 = new Senior("Senior2", "01066666666", seniorMember);
        seniorRepository.save(senior1);
        seniorRepository.save(senior2);

        HelloCall helloCall1 = new HelloCall(LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 2), 10000, 60,
                "요구사항1", senior1);
        HelloCall helloCall2 = new HelloCall(LocalDate.of(2024, 1, 3),
                LocalDate.of(2024, 1, 4), 15000, 90,
                "요구사항2", senior2);

        helloCallRepository.save(helloCall1);
        helloCallRepository.save(helloCall2);

        List<HelloCall> helloCalls = helloCallRepository.findAllBySeniorIn(List.of(senior1, senior2));

        assertThat(helloCalls).hasSize(2);
        assertThat(helloCalls.get(0).getSenior()).isEqualTo(senior1);
        assertThat(helloCalls.get(1).getSenior()).isEqualTo(senior2);
    }
}
