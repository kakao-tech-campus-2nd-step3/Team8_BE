package com.example.sinitto.guardGuideline.dto;

import com.example.sinitto.guardGuideline.entity.GuardGuideline;

public record GuardGuidelineResponse(
        Long Id,
        GuardGuideline.Type type,
        String title,
        String content) {
}
