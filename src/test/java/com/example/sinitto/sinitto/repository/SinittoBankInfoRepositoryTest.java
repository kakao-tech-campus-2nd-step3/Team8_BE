package com.example.sinitto.sinitto.repository;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.repository.MemberRepository;
import com.example.sinitto.sinitto.entity.SinittoBankInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class SinittoBankInfoRepositoryTest {

    @Autowired
    private SinittoBankInfoRepository sinittoBankInfoRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;
    private SinittoBankInfo sinittoBankInfo;

    @BeforeEach
    void setUp() {
        member = new Member("John Doe", "123-4567", "johndoe@example.com", true);
        memberRepository.save(member);

        sinittoBankInfo = new SinittoBankInfo("Bank A", "987654321", member);
        sinittoBankInfoRepository.save(sinittoBankInfo);
    }

    @Test
    @DisplayName("멤버 아이디 기반 시니또 은행 정보 불러오기 테스트")
    void testFindByMemberId() {
        Optional<SinittoBankInfo> foundSinitto = sinittoBankInfoRepository.findByMemberId(member.getId());
        assertTrue(foundSinitto.isPresent());
        assertEquals(sinittoBankInfo.getBankName(), foundSinitto.get().getBankName());
    }

    @Test
    @DisplayName("시니또 은행 정보 저장 테스트")
    void testSaveSinittoBankInfo() {
        Member newMember = new Member("new Jane Doe", "111-1111", "newjanedoe@example.com", true);
        memberRepository.save(newMember);

        SinittoBankInfo newSinittoBankInfo = new SinittoBankInfo("Bank B", "123456789", newMember);
        SinittoBankInfo savedSinittoBankInfo = sinittoBankInfoRepository.save(newSinittoBankInfo);

        assertNotNull(savedSinittoBankInfo);
        assertEquals("Bank B", savedSinittoBankInfo.getBankName());
    }

    @Test
    @DisplayName("시니또 은행 정보 삭제 테스트")
    void testDeleteSinittoBankInfo() {
        sinittoBankInfoRepository.delete(sinittoBankInfo);
        Optional<SinittoBankInfo> deletedSinitto = sinittoBankInfoRepository.findById(sinittoBankInfo.getId());
        assertFalse(deletedSinitto.isPresent());
    }

    @Test
    @DisplayName("멤버 아이디 기반 존재여부 검증 테스트")
    void testExistByMemberId() {
        boolean check1 = sinittoBankInfoRepository.existsByMemberId(member.getId());
        assertTrue(check1);

        boolean check2 = sinittoBankInfoRepository.existsByMemberId(2L);
        assertFalse(check2);
    }
}
