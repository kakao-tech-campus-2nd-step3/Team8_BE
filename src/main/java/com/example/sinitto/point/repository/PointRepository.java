package com.example.sinitto.point.repository;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByMember(Member member);
}
