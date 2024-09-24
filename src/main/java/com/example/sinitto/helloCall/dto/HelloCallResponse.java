package com.example.sinitto.helloCall.dto;

public record HelloCallResponse(
        Long helloCallId,
        String seniorName,
        boolean[] days) {
}
