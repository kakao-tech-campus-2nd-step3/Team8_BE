package com.example.sinitto.auth.service;

import com.example.sinitto.auth.exception.JWTExpirationException;
import com.example.sinitto.auth.exception.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenService {

    private static final long ACCESS_TEN_HOURS = 1000 * 60 * 60 * 10;
    private static final long REFRESH_SEVEN_DAYS = 1000 * 60 * 60 * 24 * 7;

    private final Key secretKey;

    public TokenService(@Value("${jwt.secret}") String secretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        this.secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
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
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_SEVEN_DAYS))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
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

    public String refreshAccessToken(String refreshToken) {
        try {
            var claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(refreshToken)
                    .getBody();

            if (claims.getExpiration().before(new Date())) {
                throw new JWTExpirationException("리프레쉬 토큰이 만료되었습니다.");
            }

            return generateAccessToken(claims.getSubject());
        } catch (Exception e) {
            throw new UnauthorizedException("유효하지 않은 리프레쉬 토큰입니다.");
        }
    }

}
