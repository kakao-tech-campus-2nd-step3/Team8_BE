package com.example.sinitto.point.repository;

import com.example.sinitto.member.entity.Member;
import com.example.sinitto.point.entity.PointLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointLogRepository extends JpaRepository<PointLog, Long> {

    Page<PointLog> findAllByMember(Member member, Pageable pageable);

    List<PointLog> findAllByStatusInOrderByPostTimeDesc(List<PointLog.Status> statuses);
}
