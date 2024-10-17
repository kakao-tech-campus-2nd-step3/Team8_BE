package com.example.sinitto.helloCall.repository;

import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.member.entity.Member;
import com.example.sinitto.member.entity.Senior;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HelloCallRepository extends JpaRepository<HelloCall, Long> {

    Optional<HelloCall> findBySenior(Senior senior);

    List<HelloCall> findAllByMember(Member member);

    List<HelloCall> findAllBySeniorIn(List<Senior> seniors);

    boolean existsBySeniorAndStatusIn(Senior senior, List<HelloCall.Status> statuses);
}
