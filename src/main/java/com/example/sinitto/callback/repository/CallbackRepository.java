package com.example.sinitto.callback.repository;

import com.example.sinitto.callback.entity.Callback;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CallbackRepository extends JpaRepository<Callback, Long> {

    Page<Callback> findAllByStatus(Callback.Status status, Pageable pageable);

    Optional<Callback> findByAssignedMemberIdAndStatus(Long memberId, Callback.Status status);

    Optional<Callback> findByAssignedMemberAndStatus(Member member, Callback.Status status);

    boolean existsByAssignedMemberIdAndStatus(Long memberId, Callback.Status status);

    Page<Callback> findAllBySeniorIn(List<Senior> seniors, Pageable pageable);

    List<Callback> findAllByStatusAndPendingCompleteTimeBetween(Callback.Status status, LocalDateTime startDateTime, LocalDateTime endDateTime);

    boolean existsBySeniorAndStatusIn(Senior senior, List<Callback.Status> statuses);
}
