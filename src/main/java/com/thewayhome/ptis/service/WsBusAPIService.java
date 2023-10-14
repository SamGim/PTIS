package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.wsbus.IServiceResult;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class WsBusAPIService {

    private final String apiKey;
    private final String endpoint;
    private final String pathOfGetStationByNameList;
    private final String pathOfGetStationByUid;

    public WsBusAPIService(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getstationbyname}") String pathOfGetStationByNameList,
            @Value("${openapi.arrinfo.bus.path.getstationbyuid}") String pathOfGetStationByUid
    ) {
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.pathOfGetStationByNameList = pathOfGetStationByNameList;
        this.pathOfGetStationByUid = pathOfGetStationByUid;
    }

    public Mono<IServiceResult> getStationByNameList(String stationName) {
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

        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);

        WebClient.RequestBodySpec bodySpec = uriSpec.uri(uriBuilder -> uriBuilder
                .path(pathOfGetStationByNameList)
                .queryParam("serviceKey", apiKey)
                .queryParam("stSrch", stationName)
                .build()
        );

        WebClient.ResponseSpec responseSpec = bodySpec
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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
                                JAXBContext jaxbContext = JAXBContext.newInstance(com.thewayhome.ptis.vo.wsbus.getstationbynamelist.ServiceResultNormalVoImpl.class);
                                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                                StringReader reader = new StringReader(body);
                                IServiceResult result = (com.thewayhome.ptis.vo.wsbus.getstationbynamelist.ServiceResultNormalVoImpl) unmarshaller.unmarshal(reader);

                                return Mono.just(result);
                            } catch (JAXBException e) {
                                return Mono.error(new RuntimeException(body));
                            }
                        })
                        .onErrorResume(errorBody -> {
                            try {
                                JAXBContext jaxbContext = JAXBContext.newInstance(com.thewayhome.ptis.vo.wsbus.getstationbynamelist.ServiceResultErrorVoImpl.class);
                                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                                StringReader reader = new StringReader(errorBody.getMessage());
                                IServiceResult result = (com.thewayhome.ptis.vo.wsbus.getstationbynamelist.ServiceResultErrorVoImpl) unmarshaller.unmarshal(reader);

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

    public Mono<IServiceResult> getStationByUid(String arsId) {
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

        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.GET);

        WebClient.RequestBodySpec bodySpec = uriSpec.uri(uriBuilder -> uriBuilder
                .path(pathOfGetStationByUid)
                .queryParam("serviceKey", apiKey)
                .queryParam("arsId", arsId)
                .build()
        );

        WebClient.ResponseSpec responseSpec = bodySpec
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
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
                                JAXBContext jaxbContext = JAXBContext.newInstance(com.thewayhome.ptis.vo.wsbus.getstationbyuid.ServiceResultNormalVoImpl.class);
                                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                                StringReader reader = new StringReader(body);
                                IServiceResult result = (com.thewayhome.ptis.vo.wsbus.getstationbyuid.ServiceResultNormalVoImpl) unmarshaller.unmarshal(reader);

                                return Mono.just(result);
                            } catch (JAXBException e) {
                                return Mono.error(new RuntimeException(body));
                            }
                        })
                        .onErrorResume(errorBody -> {
                            try {
                                JAXBContext jaxbContext = JAXBContext.newInstance(com.thewayhome.ptis.vo.wsbus.getstationbyuid.ServiceResultErrorVoImpl.class);
                                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                                StringReader reader = new StringReader(errorBody.getMessage());
                                IServiceResult result = (com.thewayhome.ptis.vo.wsbus.getstationbyuid.ServiceResultErrorVoImpl) unmarshaller.unmarshal(reader);

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
