package com.example.sinitto.member.repository;

import com.example.sinitto.member.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        Member expected = new Member("test", "01011112222", "test@test.com", true);
        Member actual = memberRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }

    @Test
    @DisplayName("이메일 기반으로 멤버 찾기 테스트")
    void findByEmailTest() {
        Member member = new Member("test2", "01033334444", "test2@test.com", false);
        Member actual = memberRepository.save(member);
        Optional<Member> expected = memberRepository.findByEmail(member.getEmail());

        assertThat(actual.getId()).isNotNull();
        assertThat(expected).isPresent();
        assertThat(expected.get().isSinitto()).isFalse();
    }

    @Test
    @DisplayName("저장되어있지 않은 이메일로 유저를 찾을 때 빈 Optional을 리턴하는지 테스트")
    void edgeCaseTest() {
        String NotSavedEmail = "notSavedEmail@test.com";

        assertThat(memberRepository.findByEmail(NotSavedEmail)).isEmpty();
    }

    @Test
    @DisplayName("이메일 존재 여부 테스트")
    void existsByEmailTest() {
        Member member = new Member("test3", "01055556666", "test3@test.com", true);
        memberRepository.save(member);

        boolean exists = memberRepository.existsByEmail("test3@test.com");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("저장되지 않은 이메일 존재 여부 테스트")
    void notExistsByEmailTest() {
        String NotSavedEmail = "notExistEmail@test.com";

        boolean exists = memberRepository.existsByEmail(NotSavedEmail);

        assertThat(exists).isFalse();
    }

}
