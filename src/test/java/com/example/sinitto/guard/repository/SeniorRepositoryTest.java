package com.example.sinitto.guard.repository;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class SeniorRepositoryTest {
    @Autowired
    private SeniorRepository seniorRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("저장 테스트")
    void saveSenior() {
        //given
        Member member = memberRepository.save(new Member("test", "01012345678", "test@test.com", false));
        Senior senior = new Senior("testSenior", "01000000000", member);
        //when
        Senior result = seniorRepository.save(senior);
        //then
        assertThat(senior).isSameAs(result);
    }

    @Test
    @DisplayName("조회 확인")
    void readSenior() {
        //given
        Member member = memberRepository.save(new Member("test", "01012345678", "test@test.com", false));
        Senior senior = seniorRepository.save(new Senior("testSenior", "01000000000", member));

        //when
        Senior result = seniorRepository.findById(senior.getId()).get();

        //then
        assertThat(senior).isSameAs(result);
    }

    @Test
    @DisplayName("findByMemberId 메소드 테스트")
    void findByMemberIdTest() {
        //given
        Member member = memberRepository.save(new Member("test", "01012345678", "test@test.com", false));
        Senior senior = seniorRepository.save(new Senior("testSenior", "01000000000", member));

        //when
        Senior result = seniorRepository.findByMemberId(member.getId()).get(0);

        //then
        assertThat(senior).isSameAs(result);
    }

    @Test
    @DisplayName("findByIdAndMemberId 메소드 테스트")
    void findByIdAndMemberIdTest() {
        //given
        Member member = memberRepository.save(new Member("test", "01012345678", "test@test.com", false));
        Senior senior = seniorRepository.save(new Senior("testSenior", "01000000000", member));

        //when
        Senior result = seniorRepository.findByIdAndMemberId(senior.getId(), member.getId()).get();

        //then
        assertThat(senior).isSameAs(result);
    }
}
