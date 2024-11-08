package com.example.sinitto.auth.service;

import com.example.sinitto.common.exception.BadRequestException;
import com.example.sinitto.common.properties.KakaoProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@MockitoSettings
public class KakaoApiServiceTest {
    @InjectMocks
    KakaoApiService kakaoApiService;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private KakaoProperties kakaoProperties;
    private String clientId = "testClientId";

    private String devRedirectUri = "testDevUri";

    private String redirectUri = "https://testRedirectUri";

    private String frontUriWithoutHttps = "testRedirectUri";

    @Test
    @DisplayName("getAuthorizationUrl 메소드 테스트 - devUri 포함 시")
    void getAuthorizationUrlTestWithDevUri() {
        //given
        when(httpServletRequest.getHeader("Referer")).thenReturn("http://localhost:5173");
        when(kakaoProperties.devRedirectUri()).thenReturn(devRedirectUri);
        when(kakaoProperties.clientId()).thenReturn(clientId);

        //when
        String result = kakaoApiService.getAuthorizationUrl(httpServletRequest);
        String expect = "https://kauth.kakao.com/oauth" + "/authorize?response_type=code&client_id="
                + clientId
                + "&redirect_uri="
                + devRedirectUri;

        //then
        assertEquals(result, expect);
    }

    @Test
    @DisplayName("getAuthorizationUrl 메소드 테스트 - 배포 uri 포함 시")
    void getAuthorizationUrlTestWithAwsUri() {
        //given
        when(kakaoProperties.frontUriWithoutHttps()).thenReturn(frontUriWithoutHttps);
        when(httpServletRequest.getHeader("Referer")).thenReturn("https://testRedirectUri");
        when(kakaoProperties.redirectUri()).thenReturn(redirectUri);
        when(kakaoProperties.clientId()).thenReturn(clientId);

        //when
        String result = kakaoApiService.getAuthorizationUrl(httpServletRequest);
        String expect = "https://kauth.kakao.com/oauth" + "/authorize?response_type=code&client_id="
                + clientId
                + "&redirect_uri="
                + redirectUri;

        //then
        assertEquals(result, expect);
    }

    @Test
    @DisplayName("getAuthorizationUrl 메소드 테스트 - 이외의 주소 포함 시 실패")
    void getAuthorizationUrlTestWithAnotherUri() {
        //given
        when(httpServletRequest.getHeader("Referer")).thenReturn("http://test-uri.com");
        when(kakaoProperties.frontUriWithoutHttps()).thenReturn(frontUriWithoutHttps);

        //when, then
        assertThrows(BadRequestException.class, () -> kakaoApiService.getAuthorizationUrl(httpServletRequest));
    }

    @Test
    @DisplayName("getAuthorizationUrl 메소드 테스트 - 주소가 null일 시 실패")
    void getAuthorizationUrlTestWithNullUri() {
        //given
        when(httpServletRequest.getHeader("Referer")).thenReturn(null);

        //when, then
        assertThrows(BadRequestException.class, () -> kakaoApiService.getAuthorizationUrl(httpServletRequest));
    }
}
