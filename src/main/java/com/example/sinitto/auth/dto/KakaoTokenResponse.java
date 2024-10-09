package com.example.sinitto.auth.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenResponse(
        String accessToken,
        String refreshToken,
        int expiresIn,
        int refreshTokenExpiresIn

) {

}
