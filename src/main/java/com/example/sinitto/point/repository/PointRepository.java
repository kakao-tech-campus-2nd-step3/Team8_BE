package com.example.sinitto.point.repository;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.point.entity.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByMember(Member member);

    Optional<Point> findByMemberId(Long memberId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Point p WHERE p.member.id = :memberId")
    Optional<Point> findByMemberIdWithWriteLock(Long memberId);
}
