package com.example.sinitto.common.service;

import com.example.sinitto.auth.service.KakaoTokenService;
import com.example.sinitto.common.properties.KakaoProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class KakaoMessageService {

    private static final String KAKAO_SEND_ME_BASE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private static final String SINITTO_IMAGE_URL = "https://ifh.cc/g/GhjQyC.jpg";

    private final KakaoTokenService kakaoTokenService;
    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;

    public KakaoMessageService(KakaoTokenService kakaoTokenService, RestTemplate restTemplate, KakaoProperties kakaoProperties) {
        this.kakaoTokenService = kakaoTokenService;
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
    }

    public void sendPointChargeRequestReceivedMessage(String email, int point, String name, String depositMessage) {
        String accessToken = kakaoTokenService.getValidAccessTokenInServer(email);

        if (accessToken == null) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(accessToken);

        String templateObject = String.format(
                "{" +
                        "\"object_type\": \"feed\"," +
                        "\"content\": {" +
                        "\"title\": \"%s님의 포인트 충전 요청 접수\"," +
                        "\"description\": \"입금자명 변경하여 입금해주시기 바랍니다.\"," +
                        "\"image_url\": \"%s\"," +
                        "\"image_width\": 640," +
                        "\"image_height\": 640," +
                        "\"link\": {" +
                        "\"web_url\": \"%s\"," +
                        "\"mobile_web_url\": \"%s\"" +
                        "}" +
                        "}," +
                        "\"item_content\": {" +
                        "\"items\": [" +
                        "{\"item\": \"충전 요청\", \"item_op\": \"%d points\"}," +
                        "{\"item\": \"입금 금액\", \"item_op\": \"%d 원\"}," +
                        "{\"item\": \"입금자명\", \"item_op\": \"%s\"}," +
                        "{\"item\": \"은행\", \"item_op\": \"%s\"}," +
                        "{\"item\": \"계좌번호\", \"item_op\": \"%s\"}," +
                        "{\"item\": \"성명\", \"item_op\": \"%s\"}" +
                        "]" +
                        "}," +
                        "\"buttons\": [" +
                        "{" +
                        "\"title\": \"서비스 이용하기\"," +
                        "\"link\": {" +
                        "\"web_url\": \"%s\"," +
                        "\"mobile_web_url\": \"%s\"" +
                        "}" +
                        "}" +
                        "]" +
                        "}",
                name, SINITTO_IMAGE_URL, kakaoProperties.frontUri(), kakaoProperties.frontUri(), point, point, depositMessage,
                kakaoProperties.bankName(), kakaoProperties.accountNumber(), kakaoProperties.name(), kakaoProperties.frontUri(), kakaoProperties.frontUri()
        );


        String encodedTemplateObject = URLEncoder.encode(templateObject, StandardCharsets.UTF_8);

        RequestEntity<String> request = new RequestEntity<>(
                "template_object=" + encodedTemplateObject,
                headers, HttpMethod.POST, URI.create(KAKAO_SEND_ME_BASE_URL));

        restTemplate.exchange(request, String.class);
    }

    public void sendPointChargeCompleteMessage(String email, int point, String name) {
        String accessToken = kakaoTokenService.getValidAccessTokenInServer(email);

        if (accessToken == null) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(accessToken);

        String templateObject = String.format(
                "{" +
                        "\"object_type\": \"feed\"," +
                        "\"content\": {" +
                        "\"title\": \"%s님의 포인트 충전 완료\"," +
                        "\"description\": \"서비스를 이용해주셔서 감사합니다.\"," +
                        "\"image_url\": \"%s\"," +
                        "\"image_width\": 640," +
                        "\"image_height\": 640," +
                        "\"link\": {" +
                        "\"web_url\": \"%s\"," +
                        "\"mobile_web_url\": \"%s\"" +
                        "}" +
                        "}," +
                        "\"item_content\": {" +
                        "\"items\": [" +
                        "{\"item\": \"충전된 포인트\", \"item_op\": \"%d points\"}" +
                        "]" +
                        "}," +
                        "\"buttons\": [" +
                        "{" +
                        "\"title\": \"서비스 이용하기\"," +
                        "\"link\": {" +
                        "\"web_url\": \"%s\"," +
                        "\"mobile_web_url\": \"%s\"" +
                        "}" +
                        "}" +
                        "]" +
                        "}",
                name, SINITTO_IMAGE_URL, kakaoProperties.frontUri(), kakaoProperties.frontUri(), point,
                kakaoProperties.frontUri(), kakaoProperties.frontUri()
        );

        String encodedTemplateObject = URLEncoder.encode(templateObject, StandardCharsets.UTF_8);

        RequestEntity<String> request = new RequestEntity<>(
                "template_object=" + encodedTemplateObject,
                headers, HttpMethod.POST, URI.create(KAKAO_SEND_ME_BASE_URL));

        restTemplate.exchange(request, String.class);
    }

    public void sendPointWithdrawRequestReceivedMessage(String email, int point, String name, String bankName, String accountNumber) {
        String accessToken = kakaoTokenService.getValidAccessTokenInServer(email);

        if (accessToken == null) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(accessToken);

        String templateObject = String.format(
                "{" +
                        "\"object_type\": \"feed\"," +
                        "\"content\": {" +
                        "\"title\": \"%s님의 포인트 인출 요청 접수\"," +
                        "\"description\": \"인출에는 최대 2~3영업일이 소요됩니다.\"," +
                        "\"image_url\": \"%s\"," +
                        "\"image_width\": 640," +
                        "\"image_height\": 640," +
                        "\"link\": {" +
                        "\"web_url\": \"%s\"," +
                        "\"mobile_web_url\": \"%s\"" +
                        "}" +
                        "}," +
                        "\"item_content\": {" +
                        "\"items\": [" +
                        "{\"item\": \"인출 포인트\", \"item_op\": \"%d points\"}," +
                        "{\"item\": \"인출 금액\", \"item_op\": \"%.0f 원\"}," +
                        "{\"item\": \"은행\", \"item_op\": \"%s\"}," +
                        "{\"item\": \"계좌번호\", \"item_op\": \"%s\"}," +
                        "{\"item\": \"성명\", \"item_op\": \"%s\"}" +
                        "]" +
                        "}," +
                        "\"buttons\": [" +
                        "{" +
                        "\"title\": \"서비스 이용하기\"," +
                        "\"link\": {" +
                        "\"web_url\": \"%s\"," +
                        "\"mobile_web_url\": \"%s\"" +
                        "}" +
                        "}" +
                        "]" +
                        "}",
                name, SINITTO_IMAGE_URL, kakaoProperties.frontUri(), kakaoProperties.frontUri(), point, point * 0.8,
                bankName, accountNumber, name, kakaoProperties.frontUri(), kakaoProperties.frontUri()
        );


        String encodedTemplateObject = URLEncoder.encode(templateObject, StandardCharsets.UTF_8);

        RequestEntity<String> request = new RequestEntity<>(
                "template_object=" + encodedTemplateObject,
                headers, HttpMethod.POST, URI.create(KAKAO_SEND_ME_BASE_URL));

        restTemplate.exchange(request, String.class);
    }

    public void sendPointWithdrawCompleteMessage(String email, int point, String name, String bankName, String accountNumber) {
        String accessToken = kakaoTokenService.getValidAccessTokenInServer(email);

        if (accessToken == null) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        headers.setBearerAuth(accessToken);

        String templateObject = String.format(
                "{" +
                        "\"object_type\": \"feed\"," +
                        "\"content\": {" +
                        "\"title\": \"%s님의 포인트 인출 완료\"," +
                        "\"description\": \"서비스를 이용해주셔서 감사합니다.\"," +
                        "\"image_url\": \"%s\"," +
                        "\"image_width\": 640," +
                        "\"image_height\": 640," +
                        "\"link\": {" +
                        "\"web_url\": \"%s\"," +
                        "\"mobile_web_url\": \"%s\"" +
                        "}" +
                        "}," +
                        "\"item_content\": {" +
                        "\"items\": [" +
                        "{\"item\": \"인출 포인트\", \"item_op\": \"%d points\"}," +
                        "{\"item\": \"인출 금액\", \"item_op\": \"%.0f 원\"}," +
                        "{\"item\": \"은행\", \"item_op\": \"%s\"}," +
                        "{\"item\": \"계좌번호\", \"item_op\": \"%s\"}," +
                        "{\"item\": \"성명\", \"item_op\": \"%s\"}" +
                        "]" +
                        "}," +
                        "\"buttons\": [" +
                        "{" +
                        "\"title\": \"서비스 이용하기\"," +
                        "\"link\": {" +
                        "\"web_url\": \"%s\"," +
                        "\"mobile_web_url\": \"%s\"" +
                        "}" +
                        "}" +
                        "]" +
                        "}",
                name, SINITTO_IMAGE_URL, kakaoProperties.frontUri(), kakaoProperties.frontUri(), point, point * 0.8,
                bankName, accountNumber, name, kakaoProperties.frontUri(), kakaoProperties.frontUri()
        );

        String encodedTemplateObject = URLEncoder.encode(templateObject, StandardCharsets.UTF_8);

        RequestEntity<String> request = new RequestEntity<>(
                "template_object=" + encodedTemplateObject,
                headers, HttpMethod.POST, URI.create(KAKAO_SEND_ME_BASE_URL));

        restTemplate.exchange(request, String.class);
    }

}
