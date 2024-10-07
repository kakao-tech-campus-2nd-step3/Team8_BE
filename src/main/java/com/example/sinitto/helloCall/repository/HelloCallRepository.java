package com.example.sinitto.helloCall.repository;

import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.member.entity.Senior;
import com.example.sinitto.member.entity.Sinitto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HelloCallRepository extends JpaRepository<HelloCall, Long> {

    Optional<HelloCall> findBySenior(Senior senior);

    List<HelloCall> findAllBySinitto(Sinitto sinitto);

    boolean existsBySenior(Senior senior);
}
