package com.example.sinitto.guard.repository;

import com.example.sinitto.member.entity.Senior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeniorRepository extends JpaRepository<Senior, Long> {
    List<Senior> findByMemberId(Long memberId);
    Optional<Senior> findByIdAndMemberId(Long Id, Long memberId);
    Optional<Senior> findByPhoneNumber(String phoneNumber);
}
