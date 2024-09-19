package com.example.sinitto.dto;

import java.time.LocalDateTime;

public class CallbackResponse {

    String seniorName;
    LocalDateTime postTime;
    String status;

    public String getStatus() {
        return status;
    }

    public String getSeniorName() {
        return seniorName;
    }

    public LocalDateTime getPostTime() {
        return postTime;
    }

}
