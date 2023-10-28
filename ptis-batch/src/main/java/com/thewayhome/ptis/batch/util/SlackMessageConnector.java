package com.thewayhome.ptis.batch.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SlackMessageConnector {

    private final String slackWebhookEndpoint;
    private final String slackWebhookEnabled;
    private final ObjectMapper objectMapper;

    public SlackMessageConnector(
            @Value("${webhook.slack.endpoint}") String slackWebhookEndpoint,
            @Value("${webhook.slack.enabled}") String slackWebhookEnabled,
            ObjectMapper objectMapper
    ) {
        this.slackWebhookEndpoint = slackWebhookEndpoint;
        this.slackWebhookEnabled = slackWebhookEnabled;
        this.objectMapper = objectMapper;
    }

    public void sendSlackMessage(String message)  {
        if (
                !"Y".equals(slackWebhookEnabled) &&
                !"y".equals(slackWebhookEnabled) &&
                !"Yes".equals(slackWebhookEnabled) &&
                !"yes".equals(slackWebhookEnabled) &&
                !"True".equals(slackWebhookEnabled) &&
                !"true".equals(slackWebhookEnabled)
        ) {
           return;
        }

        MultiValueMap<String, String> payload = new LinkedMultiValueMap<>();
        payload.add("text", message);

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                );

        WebClient client = WebClient.builder()
                .baseUrl(this.slackWebhookEndpoint)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        String jsonPayload = null;

        try {
            jsonPayload = objectMapper.writeValueAsString(payload.toSingleValueMap());
        } catch (JsonProcessingException e) {
            log.error("Slack 메세지 전송 실패 [" + jsonPayload + "]");
            return;
        }

        WebClient.RequestHeadersSpec<?> bodySpec = client.method(HttpMethod.POST)
                .body(BodyInserters.fromValue(jsonPayload));

        bodySpec.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .retrieve();

        bodySpec.exchangeToMono(response -> {
            if (response.statusCode().is2xxSuccessful()) {
                return response.bodyToMono(String.class);
            } else if (response.statusCode().is4xxClientError()) {
                return Mono.error(new RuntimeException("클라이언트 에러: " + response.statusCode()));
            } else {
                return Mono.error(new RuntimeException("서버 에러: " + response.statusCode()));
            }
        }).block();
    }
}
