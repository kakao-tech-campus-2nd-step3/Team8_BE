package com.example.sinitto.auth.service;

import com.example.sinitto.auth.dto.KakaoTokenResponse;
import com.example.sinitto.auth.dto.KakaoUserResponse;
import com.example.sinitto.auth.exception.KakaoEmailNotFoundException;
import com.example.sinitto.common.properties.KakaoProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
public class KakaoApiService {

    private static final String KAKAO_AUTH_BASE_URL = "https://kauth.kakao.com/oauth";
    private static final String KAKAO_API_BASE_URL = "https://kapi.kakao.com/v2/user";
    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;

    public KakaoApiService(RestTemplate restTemplate, KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
    }

    public String getAuthorizationUrl() {
        return KAKAO_AUTH_BASE_URL + "/authorize?response_type=code&client_id="
                + kakaoProperties.clientId() + "&redirect_uri=" + kakaoProperties.redirectUri();
    }

    public KakaoTokenResponse getAccessToken(String authorizationCode) {
        String url = KAKAO_AUTH_BASE_URL + "/token";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", authorizationCode);

        RequestEntity<LinkedMultiValueMap<String, String>> request = new RequestEntity<>(body,
                headers, HttpMethod.POST, URI.create(url));

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(request,
                KakaoTokenResponse.class);

        return response.getBody();
    }

    public KakaoTokenResponse refreshAccessToken(String refreshToken) {
        String url = KAKAO_AUTH_BASE_URL + "/token";
        String body = "grant_type=refresh_token&client_id=" + kakaoProperties.clientId()
                + "&refresh_token=" + refreshToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, KakaoTokenResponse.class);

        return response.getBody();
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        String url = KAKAO_API_BASE_URL + "/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("property_keys", "[\"kakao_account.email\", \"kakao_account.profile\"]");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, request, KakaoUserResponse.class);

        if (response.getBody().kakaoAccount().email() == null) {
            throw new KakaoEmailNotFoundException("카카오 계정으로부터 전달받은 이메일이 없습니다.");
        }

        return response.getBody();
    }
}
