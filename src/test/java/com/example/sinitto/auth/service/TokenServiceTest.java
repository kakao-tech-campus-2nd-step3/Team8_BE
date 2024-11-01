package com.example.sinitto.auth.service;
import com.example.sinitto.common.exception.InvalidJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings
public class TokenServiceTest {
    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private TokenService tokenService;

    String key = "thisistestkeynotrealkeythisistestkeynotrealkey";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        String secretKey = Base64.getEncoder().encodeToString(this.key.getBytes());
        tokenService = new TokenService(secretKey, redisTemplate);
    }

    @Test
    @DisplayName("generateAccessToken 메소드 테스트 - extractEmail 메소드도 함께 사용")
    void generateAccessTokenTest() {
        //given
        String email = "test@email.com";

        //when
        String token = tokenService.generateAccessToken(email);
        String resultEmail = tokenService.extractEmail(token);

        //then
        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
        assertEquals(email, resultEmail);
    }

    @Test
    @DisplayName("generateRefreshToken 메소드 테스트 - extractEmail 메소드도 함께 사용")
    void generateRefreshTokenTest(){
        //given
        String email = "test@email.com";

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        //when
        String token = tokenService.generateRefreshToken(email);
        String resultEmail = tokenService.extractEmail(token);

        //then
        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
        assertEquals(email, resultEmail);
    }
}
