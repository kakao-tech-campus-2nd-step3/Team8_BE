package com.example.sinitto.guardGuideline.dto;

import com.example.sinitto.guardGuideline.entity.GuardGuideline.Type;

public record GuardGuidelineRequest(
        Long seniorId,
        Type type,
        String title,
        String content
) {}
