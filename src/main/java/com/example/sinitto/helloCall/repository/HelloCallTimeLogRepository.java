package com.example.sinitto.helloCall.repository;

import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.HelloCallTimeLog;
import com.example.sinitto.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HelloCallTimeLogRepository extends JpaRepository<HelloCallTimeLog, Long> {
    List<HelloCallTimeLog> findAllByHelloCallId(Long helloCallId);

    Optional<HelloCallTimeLog> findByMemberAndHelloCallId(Member member, Long helloCallId);

    Optional<HelloCallTimeLog> findTopByMemberAndHelloCallOrderByStartDateAndTimeDesc(Member member, HelloCall helloCall);
}
