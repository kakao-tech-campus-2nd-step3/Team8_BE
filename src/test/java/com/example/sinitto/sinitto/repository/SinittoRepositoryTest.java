package com.example.sinitto.sinitto.repository;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Sinitto;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SinittoRepositoryTest {

    @Autowired
    private SinittoRepository sinittoRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private Sinitto sinitto;

    @BeforeEach
    void setUp() {
        member = new Member("John Doe", "123-4567", "johndoe@example.com", true);
        memberRepository.save(member);

        // Member만으로 생성하는 생성자 사용
        sinitto = new Sinitto(member);
        sinittoRepository.save(sinitto);
    }

    @Test
    @DisplayName("회원 ID로 Sinitto 찾기 테스트")
    void testFindByMemberId() {
        Optional<Sinitto> foundSinitto = sinittoRepository.findByMemberId(member.getId());
        assertTrue(foundSinitto.isPresent());
        assertEquals(sinitto.getMember(), foundSinitto.get().getMember());
    }

    @Test
    @DisplayName("Sinitto 저장 테스트")
    void testSaveSinitto() {
        Member newMember = new Member("Jane Doe", "987-6543", "janedoe@example.com", true);
        memberRepository.save(newMember);
        Sinitto newSinitto = new Sinitto(newMember);
        Sinitto savedSinitto = sinittoRepository.save(newSinitto);
        assertNotNull(savedSinitto);
        assertEquals(newMember, savedSinitto.getMember());
    }

    @Test
    @DisplayName("Sinitto 삭제 테스트")
    void testDeleteSinitto() {
        sinittoRepository.delete(sinitto);
        Optional<Sinitto> deletedSinitto = sinittoRepository.findById(sinitto.getId());
        assertFalse(deletedSinitto.isPresent());
    }
}
