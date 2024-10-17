package com.example.sinitto.sinitto.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Sinitto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SinittoTest {

    private Sinitto sinitto;
    private Member member;

    @BeforeEach
    void setUp() {
        member = new Member("John Doe", "123-4567", "johndoe@example.com", true);
        sinitto = new Sinitto("Bank A", "987654321", member);
    }

    @Test
    @DisplayName("Sinitto 생성 테스트")
    void testCreateSinitto() {
        assertNotNull(sinitto);
        assertEquals("Bank A", sinitto.getBankName());
        assertEquals("987654321", sinitto.getAccountNumber());
        assertEquals(member, sinitto.getMember());
    }

    @Test
    @DisplayName("Member만으로 Sinitto 생성 테스트")
    void testCreateSinittoWithOnlyMember() {
        Sinitto sinittoWithOnlyMember = new Sinitto(member);

        assertNotNull(sinittoWithOnlyMember);
        assertEquals(member, sinittoWithOnlyMember.getMember());
        assertNull(sinittoWithOnlyMember.getBankName());
        assertNull(sinittoWithOnlyMember.getAccountNumber());
    }

    @Test
    @DisplayName("Sinitto 업데이트 테스트")
    void testUpdateSinitto() {
        sinitto.updateSinitto("Bank B", "123456789");

        assertEquals("Bank B", sinitto.getBankName());
        assertEquals("123456789", sinitto.getAccountNumber());
    }

    @Test
    @DisplayName("Sinitto Getter 테스트")
    void testGetters() {
        assertEquals("Bank A", sinitto.getBankName());
        assertEquals("987654321", sinitto.getAccountNumber());
        assertEquals(member, sinitto.getMember());
    }
}
