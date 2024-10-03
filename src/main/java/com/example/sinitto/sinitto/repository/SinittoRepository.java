package com.example.sinitto.sinitto.repository;

import com.example.sinitto.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.sinitto.member.entity.Sinitto;

import java.util.Optional;


@Repository
public interface SinittoRepository extends JpaRepository<Sinitto, Long> {
    Optional<Sinitto> findByMemberId(Long memberId);

}