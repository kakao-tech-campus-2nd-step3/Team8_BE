package com.example.sinitto.sinitto.repository;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Sinitto;
import com.example.sinitto.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
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

        sinitto = new Sinitto("Bank A", "987654321", member);
        sinittoRepository.save(sinitto);
    }

    @Test
    void testFindByMemberId() {
        Optional<Sinitto> foundSinitto = sinittoRepository.findByMemberId(member.getId());
        assertTrue(foundSinitto.isPresent());
        assertEquals(sinitto.getBankName(), foundSinitto.get().getBankName());
    }

    @Test
    void testSaveSinitto() {
        Member newMember = new Member("Jane Doe", "987-6543", "janedoe@example.com", true);
        memberRepository.save(newMember);

        Sinitto newSinitto = new Sinitto("Bank B", "123456789", newMember);
        Sinitto savedSinitto = sinittoRepository.save(newSinitto);
        assertNotNull(savedSinitto);
        assertEquals("Bank B", savedSinitto.getBankName());
    }


    @Test
    void testDeleteSinitto() {
        sinittoRepository.delete(sinitto);
        Optional<Sinitto> deletedSinitto = sinittoRepository.findById(sinitto.getId());
        assertFalse(deletedSinitto.isPresent());
    }
}
