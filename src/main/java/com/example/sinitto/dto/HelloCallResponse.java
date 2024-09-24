package com.example.sinitto.dto;

public record HelloCallResponse(
        Long helloCallId,
        String seniorName,
        boolean[] days) {
}
