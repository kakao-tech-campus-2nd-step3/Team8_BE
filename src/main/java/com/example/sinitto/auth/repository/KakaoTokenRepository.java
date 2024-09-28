package com.example.sinitto.auth.repository;

import com.example.sinitto.auth.entity.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {
    Optional<KakaoToken> findByMemberEmail(String email);
}
