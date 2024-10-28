package com.example.sinitto.callback.repository;

import com.example.sinitto.callback.entity.Callback;
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

    boolean existsByAssignedMemberIdAndStatus(Long memberId, Callback.Status status);

    Page<Callback> findAllBySeniorIn(List<Senior> seniors, Pageable pageable);

    List<Callback> findAllByStatusAndPendingCompleteTimeBefore(Callback.Status status, LocalDateTime dateTime);

    boolean existsBySeniorAndStatusIn(Senior senior, List<Callback.Status> statuses);
}
