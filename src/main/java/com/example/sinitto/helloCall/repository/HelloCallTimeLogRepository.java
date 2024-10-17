package com.example.sinitto.helloCall.repository;

import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.HelloCallTimeLog;
import com.example.sinitto.sinitto.entity.SinittoBankInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HelloCallTimeLogRepository extends JpaRepository<HelloCallTimeLog, Long> {
    Optional<HelloCallTimeLog> findBySinittoAndHelloCallId(SinittoBankInfo sinittoBankInfo, Long helloCallId);

    List<HelloCallTimeLog> findAllByHelloCallId(Long helloCallId);

    Optional<HelloCallTimeLog> findTopBySinittoAndHelloCallOrderByStartDateAndTimeDesc(SinittoBankInfo sinittoBankInfo, HelloCall helloCall);
}
