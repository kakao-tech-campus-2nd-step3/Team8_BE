package com.example.sinitto.sinitto.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.sinitto.repository.SinittoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
class SinittoTest {

    @Mock
    private SinittoRepository sinittoRepository;

    private SinittoBankInfo sinittoBankInfo;
    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new Member("John Doe", "123-4567", "johndoe@example.com", true);
        sinittoBankInfo = new SinittoBankInfo("Bank A", "987654321", member);
    }

    @Test
    void testCreateSinitto() {
        when(sinittoRepository.save(sinittoBankInfo)).thenReturn(sinittoBankInfo);

        SinittoBankInfo savedSinittoBankInfo = sinittoRepository.save(sinittoBankInfo);

        assertNotNull(savedSinittoBankInfo);
        assertEquals("Bank A", savedSinittoBankInfo.getBankName());
        assertEquals("987654321", savedSinittoBankInfo.getAccountNumber());
        assertEquals(member, savedSinittoBankInfo.getMember());

        verify(sinittoRepository, times(1)).save(sinittoBankInfo);
    }

    @Test
    void testUpdateSinitto() {
        sinittoBankInfo.updateSinitto("Bank B", "123456789");

        when(sinittoRepository.save(sinittoBankInfo)).thenReturn(sinittoBankInfo);

        SinittoBankInfo updatedSinittoBankInfo = sinittoRepository.save(sinittoBankInfo);

        assertNotNull(updatedSinittoBankInfo);
        assertEquals("Bank B", updatedSinittoBankInfo.getBankName());
        assertEquals("123456789", updatedSinittoBankInfo.getAccountNumber());

        verify(sinittoRepository, times(1)).save(sinittoBankInfo);
    }

    @Test
    void testFindSinittoById() {
        when(sinittoRepository.findById(1L)).thenReturn(Optional.of(sinittoBankInfo));

        Optional<SinittoBankInfo> foundSinitto = sinittoRepository.findById(1L);

        assertTrue(foundSinitto.isPresent());
        assertEquals(sinittoBankInfo.getId(), foundSinitto.get().getId());
    }

    @Test
    void testDeleteSinitto() {
        sinittoRepository.delete(sinittoBankInfo);
        verify(sinittoRepository, times(1)).delete(sinittoBankInfo);
    }
}
