package com.example.sinitto.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class SlackMessageService {

    @Value("${slack.webhook.url:#{null}}}")
    private String slackWebhookUrl;

    @Value("${slack.charge.request.url:#{null}}}")
    private String chargeRequestUrl;

    @Value("${slack.withdraw.request.url:#{null}}}")
    private String withdrawRequestUrl;

    private final RestTemplate restTemplate;

    public SlackMessageService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendStyledSlackMessage(String title, String description, String actionType) {
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String actionUrl = actionType.equals("충전") ? chargeRequestUrl : withdrawRequestUrl;

        String payload = String.format(
                "{" +
                        "\"blocks\": [" +
                        "{" +
                        "\"type\": \"section\"," +
                        "\"block_id\": \"charge_request\"," +
                        "\"text\": {" +
                        "\"type\": \"mrkdwn\"," +
                        "\"text\": \"*%s*\\n%s\\n*요청 시간:* %s\"" +
                        "}," +
                        "\"accessory\": {" +
                        "\"type\": \"button\"," +
                        "\"text\": {" +
                        "\"type\": \"plain_text\"," +
                        "\"text\": \"%s 요청\"" +
                        "}," +
                        "\"url\": \"%s\"," +
                        "\"style\": \"%s\"" +
                        "}" +
                        "}" +
                        "]" +
                        "}",
                title, description, currentTime, actionType, actionUrl, actionType.equals("충전") ? "primary" : "danger"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        restTemplate.exchange(slackWebhookUrl, HttpMethod.POST, entity, String.class);
    }
}
