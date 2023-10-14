package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.wsbus.IServiceResult;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

public abstract class AbstractWsBusAPIService {

    private final String apiKey;
    private final String endpoint;
    private final String path;
    private final Class<?> serviceResultNormalClass;
    private final Class<?> serviceResultErrorClass;

    protected AbstractWsBusAPIService(
            String apiKey,
            String endpoint,
            String path,
            Class<?> serviceResultNormalClass,
            Class<?> serviceResultErrorClass
    ) {
        this.apiKey = apiKey;
        this.endpoint = endpoint;
        this.path = path;
        this.serviceResultNormalClass = serviceResultNormalClass;
        this.serviceResultErrorClass = serviceResultErrorClass;

    }

    public Mono<IServiceResult> getDataFromOpenAPI(MultiValueMap<String, String> queryParams) {
        queryParams.add("serviceKey", this.apiKey);

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                );

        WebClient client = WebClient.builder()
                .baseUrl(endpoint)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        WebClient.RequestBodySpec bodySpec = client.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder
                        .path(this.path)
                        .queryParams(queryParams)
                        .build()
                );

        bodySpec.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML)
                .acceptCharset(StandardCharsets.UTF_8)
                .ifNoneMatch("*")
                .ifModifiedSince(ZonedDateTime.now())
                .retrieve();

        return bodySpec.exchangeToMono(response -> {
            if (response.statusCode().is2xxSuccessful()) {
                return response
                        .bodyToMono(String.class)
                        .flatMap(body -> {
                            try {
                                JAXBContext jaxbContext = JAXBContext.newInstance(serviceResultNormalClass);
                                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                                StringReader reader = new StringReader(body);
                                IServiceResult result = (IServiceResult) unmarshaller.unmarshal(reader);

                                return Mono.just(result);
                            } catch (JAXBException e) {
                                return Mono.error(new RuntimeException(body));
                            }
                        })
                        .onErrorResume(errorBody -> {
                            try {
                                JAXBContext jaxbContext = JAXBContext.newInstance(serviceResultErrorClass);
                                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                                StringReader reader = new StringReader(errorBody.getMessage());
                                IServiceResult result = (IServiceResult) unmarshaller.unmarshal(reader);

                                return Mono.just(result);
                            } catch (JAXBException e) {
                                return Mono.error(new RuntimeException("XML 언마셜링 실패: " + e.getMessage(), e));
                            }
                        });
            } else if (response.statusCode().is4xxClientError()) {
                return Mono.error(new RuntimeException("클라이언트 에러: " + response.statusCode()));
            } else {
                return Mono.error(new RuntimeException("서버 에러: " + response.statusCode()));
            }
        });
    }
}
