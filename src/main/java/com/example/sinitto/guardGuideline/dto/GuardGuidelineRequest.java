package com.example.sinitto.guardGuideline.dto;

import com.example.sinitto.guardGuideline.entity.GuardGuideline;

public record GuardGuidelineRequest(
        Long seniorId,
        GuardGuideline.Type type,
        String title,
        String content
) {}
