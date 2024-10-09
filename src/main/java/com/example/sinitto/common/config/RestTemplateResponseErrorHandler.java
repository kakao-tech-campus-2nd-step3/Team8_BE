package com.example.sinitto.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(
            RestTemplateResponseErrorHandler.class);

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        var statusCode = httpResponse.getStatusCode();
        return statusCode.is4xxClientError() || statusCode.is5xxServerError();
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        var statusCode = httpResponse.getStatusCode();
        String responseBody = new String(httpResponse.getBody().readAllBytes());

        if (statusCode.is4xxClientError()) {
            LOGGER.error("클라이언트 에러: {} - Response body: {}", statusCode, responseBody);
            throw new HttpClientErrorException(statusCode, httpResponse.getStatusText(),
                    httpResponse.getHeaders(), responseBody.getBytes(), null);
        }

        if (statusCode.is5xxServerError()) {
            LOGGER.error("서버 에러: {} - Response body: {}", statusCode, responseBody);
            throw new HttpServerErrorException(statusCode, httpResponse.getStatusText(),
                    httpResponse.getHeaders(), responseBody.getBytes(), null);
        }
    }
}
