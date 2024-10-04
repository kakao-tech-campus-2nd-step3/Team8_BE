package com.example.sinitto.auth.service;

import com.example.sinitto.auth.dto.TokenResponse;
import com.example.sinitto.auth.exception.JWTExpirationException;
import com.example.sinitto.auth.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    private static final long ACCESS_TEN_HOURS = 1000 * 60 * 60 * 10;
    private static final long REFRESH_SEVEN_DAYS = 1000 * 60 * 60 * 24 * 7;

    private final Key secretKey;
    private final RedisTemplate<String, String> redisTemplate;

    public TokenService(@Value("${jwt.secret}") String secretKey, RedisTemplate<String, String> redisTemplate) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
        this.redisTemplate = redisTemplate;
    }

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TEN_HOURS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        String refreshToken = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_SEVEN_DAYS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        redisTemplate.opsForValue().set(email, refreshToken, REFRESH_SEVEN_DAYS, TimeUnit.MILLISECONDS);
        return refreshToken;
    }


    public String extractEmail(String token) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.getExpiration().before(new Date())) {
                throw new JWTExpirationException("엑세스 토큰이 만료되었습니다.");
            }

            return claims.getSubject();
        } catch (Exception e) {
            throw new UnauthorizedException("유효하지 않은 엑세스 토큰입니다.");
        }
    }

    public TokenResponse refreshAccessToken(String refreshToken) {
        var claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody();

        if (claims.getExpiration().before(new Date())) {
            throw new JWTExpirationException("리프레쉬 토큰이 만료되었습니다. 재로그인이 필요합니다.");
        }

        String storedRefreshToken = redisTemplate.opsForValue().get(claims.getSubject());
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new UnauthorizedException("만료되거나 이미 한번 사용된 리프레쉬 토큰입니다. 재로그인이 필요합니다.");
        }

        redisTemplate.delete(claims.getSubject());

        String newAccessToken = generateAccessToken(claims.getSubject());
        String newRefreshToken = generateRefreshToken(claims.getSubject());

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
