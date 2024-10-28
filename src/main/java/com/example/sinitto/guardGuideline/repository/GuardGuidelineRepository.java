package com.example.sinitto.guardGuideline.repository;

import com.example.sinitto.guardGuideline.entity.GuardGuideline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GuardGuidelineRepository extends JpaRepository<GuardGuideline, Long> {
    List<GuardGuideline> findBySeniorId(Long seniorId);

    List<GuardGuideline> findBySeniorIdAndType(Long seniorId, GuardGuideline.Type type);
}
