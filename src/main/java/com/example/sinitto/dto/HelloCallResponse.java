package com.example.sinitto.dto;

public class HelloCallResponse {

    Long helloCallId;
    String seniorName;
    boolean[] days = new boolean[7];

    public Long getHelloCallId() {
        return helloCallId;
    }

    public String getSeniorName() {
        return seniorName;
    }

    public boolean[] getDays() {
        return days;
    }
}
