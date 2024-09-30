package com.example.sinitto.member.repository;

import com.example.sinitto.member.entity.Senior;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeniorRepository extends JpaRepository<Senior, Long> {

    Optional<Senior> findByPhoneNumber(String phoneNumber);
}
