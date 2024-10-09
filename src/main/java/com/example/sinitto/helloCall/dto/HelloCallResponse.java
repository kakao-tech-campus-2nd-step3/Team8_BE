package com.example.sinitto.helloCall.dto;

import com.example.sinitto.helloCall.entity.HelloCall;

import java.util.List;

public record HelloCallResponse(
        Long helloCallId,
        String seniorName,
        List<String> days,
        HelloCall.Status status) {
}
