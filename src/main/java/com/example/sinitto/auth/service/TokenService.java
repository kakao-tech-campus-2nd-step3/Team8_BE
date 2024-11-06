package com.example.sinitto.auth.service;

import com.example.sinitto.auth.dto.TokenResponse;
import com.example.sinitto.common.exception.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    private static final long ACCESS_FIVE_MINUTES = 1000 * 60 * 5;

    private static final long REFRESH_SEVEN_DAYS = 1000 * 60 * 60 * 24 * 7;

    private final Key secretKey;
    private final RedisTemplate<String, Object> redisTemplate;

    public TokenService(@Value("${jwt.secret}") String secretKey, RedisTemplate<String, Object> redisTemplate) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
        this.redisTemplate = redisTemplate;
    }

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("tokenType", "access")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_FIVE_MINUTES))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        DataType keyType = redisTemplate.type(email);
        if (keyType != null && keyType != DataType.HASH) {
            redisTemplate.delete(email);
        }
        String refreshToken = Jwts.builder()
                .setSubject(email)
                .claim("tokenType", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_SEVEN_DAYS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        redisTemplate.opsForHash().put(email, "refreshToken", refreshToken);
        redisTemplate.opsForHash().put(email, "createdAt", System.currentTimeMillis());

        redisTemplate.expire(email, REFRESH_SEVEN_DAYS, TimeUnit.MILLISECONDS);
        return refreshToken;
    }


    public String extractEmailFromAccessToken(String accessToken) {
        Claims claims = parseClaims(accessToken);
        if (!"access".equals(claims.get("tokenType", String.class))) {
            throw new BadRequestException("사용된 토큰이 엑세스 토큰이 아닙니다. 요청하신 로직에서는 엑세스 토큰으로만 처리가 가능합니다.");
        }
        if (claims.getExpiration().before(new Date())) {
            throw new AccessTokenExpiredException("액세스 토큰이 만료되었습니다. 리프레시 토큰으로 다시 액세스 토큰을 발급받으세요.");
        }
        return claims.getSubject();
    }

    public String extractEmailFromRefreshToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        if (!"refresh".equals(claims.get("tokenType", String.class))) {
            throw new BadRequestException("해당 토큰은 리프레쉬 토큰이 아닙니다. 요청하신 로직에서는 리프레쉬 토큰만 사용이 가능합니다.");
        }
        return claims.getSubject();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new AccessTokenExpiredException(e.getMessage());
        }
    }

    public TokenResponse refreshAccessToken(String refreshToken) {
        String email = extractEmailFromRefreshToken(refreshToken);

        String storedRefreshToken = (String) redisTemplate.opsForHash().get(email, "refreshToken");
        Long createdAt = Long.parseLong((String) redisTemplate.opsForHash().get(email, "createdAt"));

        if (((System.currentTimeMillis() - createdAt) / 1000 / 60) < 1) {
            throw new ConflictException("1분 이내에 발급받은 refresh Token이 있습니다.");
        }

        if (storedRefreshToken == null) {
            throw new InvalidJwtException("토큰이 만료되었습니다. 재로그인이 필요합니다.");
        }

        if (!storedRefreshToken.equals(refreshToken)) {
            throw new RefreshTokenStolenException("이미 한번 사용된 리프레시 토큰입니다. 리프레시 토큰이 탈취되었을 가능성이 있습니다.");
        }

        redisTemplate.delete(email);

        String newAccessToken = generateAccessToken(email);
        String newRefreshToken = generateRefreshToken(email);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public void deleteAllDataFromRedis(){
        redisTemplate.getConnectionFactory()
                .getConnection()
                .serverCommands()
                .flushAll();
    }
}
