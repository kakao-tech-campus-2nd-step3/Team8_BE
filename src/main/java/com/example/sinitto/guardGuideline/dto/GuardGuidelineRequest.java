package com.example.sinitto.guardGuideline.dto;

public record GuardGuidelineRequest(
        Long seniorId,
        String type,
        String title,
        String content
) {}
