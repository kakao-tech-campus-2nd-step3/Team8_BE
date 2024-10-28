package com.example.sinitto.sinitto.repository;

import com.example.sinitto.sinitto.entity.SinittoBankInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface SinittoBankInfoRepository extends JpaRepository<SinittoBankInfo, Long> {
    Optional<SinittoBankInfo> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);

}
