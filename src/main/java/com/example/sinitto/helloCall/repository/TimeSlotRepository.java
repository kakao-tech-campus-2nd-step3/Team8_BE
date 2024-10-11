package com.example.sinitto.helloCall.repository;

import com.example.sinitto.helloCall.entity.HelloCall;
import com.example.sinitto.helloCall.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    Optional<TimeSlot> findByHelloCallAndDayName(HelloCall helloCall, String dayName);

    void deleteAllByHelloCall(HelloCall helloCall);

    List<TimeSlot> findAllByHelloCall(HelloCall helloCall);
}
