package com.example.sinitto.callback.repository;

import com.example.sinitto.callback.entity.Callback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CallbackRepository extends JpaRepository<Callback, Long> {

    Page<Callback> findAll(Pageable pageable);

    Optional<Callback> findByAssignedMemberIdAndStatus(Long memberId, Callback.Status status);
}
