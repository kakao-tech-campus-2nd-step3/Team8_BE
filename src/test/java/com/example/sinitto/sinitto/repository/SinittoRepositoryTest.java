package com.example.sinitto.sinitto.repository;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.sinitto.entity.SinittoBankInfo;
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
    private SinittoBankInfo sinittoBankInfo;

    @BeforeEach
    void setUp() {
        member = new Member("John Doe", "123-4567", "johndoe@example.com", true);
        memberRepository.save(member);

        sinittoBankInfo = new SinittoBankInfo("Bank A", "987654321", member);
        sinittoRepository.save(sinittoBankInfo);
    }

    @Test
    void testFindByMemberId() {
        Optional<SinittoBankInfo> foundSinitto = sinittoRepository.findByMemberId(member.getId());
        assertTrue(foundSinitto.isPresent());
        assertEquals(sinittoBankInfo.getBankName(), foundSinitto.get().getBankName());
    }

    @Test
    void testSaveSinitto() {
        Member newMember = new Member("Jane Doe", "987-6543", "janedoe@example.com", true);
        memberRepository.save(newMember);

        SinittoBankInfo newSinittoBankInfo = new SinittoBankInfo("Bank B", "123456789", newMember);
        SinittoBankInfo savedSinittoBankInfo = sinittoRepository.save(newSinittoBankInfo);
        assertNotNull(savedSinittoBankInfo);
        assertEquals("Bank B", savedSinittoBankInfo.getBankName());
    }


    @Test
    void testDeleteSinitto() {
        sinittoRepository.delete(sinittoBankInfo);
        Optional<SinittoBankInfo> deletedSinitto = sinittoRepository.findById(sinittoBankInfo.getId());
        assertFalse(deletedSinitto.isPresent());
    }
}
