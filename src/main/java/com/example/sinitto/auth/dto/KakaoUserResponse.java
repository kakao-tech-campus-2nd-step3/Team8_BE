package com.example.sinitto.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoUserResponse(
        Long id,
        String connectedAt,
        KakaoAccount kakaoAccount) {

    public record KakaoAccount(
            Profile profile,
            boolean isEmailValid,
            boolean isEmailVerified,
            String email) {

        public record Profile(
                String nickname) {

        }
    }
}
