package com.example.sinitto.auth.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenResponse(
        String accessToken,
        String refreshToken,
        @NotNull
        int expiresIn,
        @NotNull
        int refreshTokenExpiresIn

) {

}
