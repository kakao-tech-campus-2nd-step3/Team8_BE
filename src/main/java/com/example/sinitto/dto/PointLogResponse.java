package com.example.sinitto.dto;

import java.time.LocalDateTime;

public class PointLogResponse {

    LocalDateTime postTime;
    String requirementContent;
    int price;
    String type;

    public LocalDateTime getPostTime() {
        return postTime;
    }

    public String getRequirementContent() {
        return requirementContent;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

}
