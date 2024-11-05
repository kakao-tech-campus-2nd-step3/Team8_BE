package com.example.sinitto.auth.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@MockitoSettings
public class TokenServiceTest {
    String key = "thisistestkeynotrealkeythisistestkeynotrealkey";
    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    private TokenService tokenService;

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
        String resultEmail = tokenService.extractEmailFromAccessToken(token);

        //then
        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
        assertEquals(email, resultEmail);
    }

    @Test
    @DisplayName("generateRefreshToken 메소드 테스트 - extractEmail 메소드도 함께 사용")
    void generateRefreshTokenTest() {
        //given
        String email = "test@email.com";

        when(redisTemplate.opsForHash()).thenReturn(hashOperations);

        //when
        String token = tokenService.generateRefreshToken(email);
        String resultEmail = tokenService.extractEmailFromRefreshToken(token);

        //then
        assertNotNull(token);
        assertTrue(token.startsWith("ey"));
        assertEquals(email, resultEmail);
    }
}
