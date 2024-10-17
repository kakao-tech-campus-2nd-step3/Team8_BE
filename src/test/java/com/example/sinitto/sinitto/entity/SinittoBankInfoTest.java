package com.example.sinitto.sinitto.entity;

import com.example.sinitto.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SinittoBankInfoTest {

    private SinittoBankInfo sinittoBankInfo;
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("John Doe", "123-4567", "johndoe@example.com", true);
        sinittoBankInfo = new SinittoBankInfo("Bank A", "987654321", member);
    }

    @Test
    @DisplayName("SinittoBankInfo 생성자 테스트")
    void testCreateSinittoBankInfo() {
        SinittoBankInfo savedSinittoBankInfo = new SinittoBankInfo("new bank", "111-1111-1111", member);

        assertNotNull(savedSinittoBankInfo);
        assertEquals("new bank", savedSinittoBankInfo.getBankName());
        assertEquals("111-1111-1111", savedSinittoBankInfo.getAccountNumber());
        assertEquals(member, savedSinittoBankInfo.getMember());
    }

    @Test
    @DisplayName("SinittoBankInfo 업데이트 테스트")
    void testUpdateSinittoBankInfo() {
        sinittoBankInfo.updateSinitto("Bank B", "123456789");

        assertNotNull(sinittoBankInfo);
        assertEquals("Bank B", sinittoBankInfo.getBankName());
        assertEquals("123456789", sinittoBankInfo.getAccountNumber());
    }
}
