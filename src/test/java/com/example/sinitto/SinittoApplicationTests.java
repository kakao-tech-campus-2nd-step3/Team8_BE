package com.example.sinitto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest
class SinittoApplicationTests {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${slack.webhook.url}")
    private String slackWebhookUrl;

    @Value("${slack.charge.request.url}")
    private String chargeRequestUrl;

    @Value("${slack.withdraw.request.url}")
    private String withdrawRequestUrl;

    @Test
    void contextLoads() {
        assertNotNull(jwtSecret);
    }

}
