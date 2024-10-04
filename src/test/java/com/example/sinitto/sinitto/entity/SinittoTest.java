package com.example.sinitto.sinitto.entity;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Sinitto;
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

    private Sinitto sinitto;
    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        member = new Member("John Doe", "123-4567", "johndoe@example.com", true);
        sinitto = new Sinitto("Bank A", "987654321", member);
    }

    @Test
    void testCreateSinitto() {
        when(sinittoRepository.save(sinitto)).thenReturn(sinitto);

        Sinitto savedSinitto = sinittoRepository.save(sinitto);

        assertNotNull(savedSinitto);
        assertEquals("Bank A", savedSinitto.getBankName());
        assertEquals("987654321", savedSinitto.getAccountNumber());
        assertEquals(member, savedSinitto.getMember());

        verify(sinittoRepository, times(1)).save(sinitto);
    }

    @Test
    void testUpdateSinitto() {
        sinitto.updateSinitto("Bank B", "123456789");

        when(sinittoRepository.save(sinitto)).thenReturn(sinitto);

        Sinitto updatedSinitto = sinittoRepository.save(sinitto);

        assertNotNull(updatedSinitto);
        assertEquals("Bank B", updatedSinitto.getBankName());
        assertEquals("123456789", updatedSinitto.getAccountNumber());

        verify(sinittoRepository, times(1)).save(sinitto);
    }

    @Test
    void testFindSinittoById() {
        when(sinittoRepository.findById(1L)).thenReturn(Optional.of(sinitto));

        Optional<Sinitto> foundSinitto = sinittoRepository.findById(1L);

        assertTrue(foundSinitto.isPresent());
        assertEquals(sinitto.getId(), foundSinitto.get().getId());
    }

    @Test
    void testDeleteSinitto() {
        sinittoRepository.delete(sinitto);
        verify(sinittoRepository, times(1)).delete(sinitto);
    }
}
