package com.example.sinitto.helloCall.repository;

import com.example.sinitto.helloCall.entity.HelloCallTimeLog;
import com.example.sinitto.member.entity.Sinitto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HelloCallTimeLogRepository extends JpaRepository<HelloCallTimeLog, Long> {
    Optional<HelloCallTimeLog> findBySinittoAndAndHelloCallId(Sinitto sinitto, Long helloCallId);

    List<HelloCallTimeLog> findAllByHelloCallId(Long helloCallId);
}
