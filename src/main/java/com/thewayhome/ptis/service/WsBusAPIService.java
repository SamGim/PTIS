package com.thewayhome.ptis.service;

import com.thewayhome.ptis.vo.wsbus.getstationbynamelist.IServiceResult;
import com.thewayhome.ptis.vo.wsbus.getstationbynamelist.ServiceResultErrorVoImpl;
import com.thewayhome.ptis.vo.wsbus.getstationbynamelist.ServiceResultNormalVoImpl;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class WsBusAPIService {

    private final String apiKey;
    private final String endpoint;
    private final String pathOfGetStationByNameList;

    public WsBusAPIService(
            @Value("${openapi.arrinfo.bus.endpoint}") String endpoint,
            @Value("${openapi.data-gov.key.decoding}") String apiKey,
            @Value("${openapi.arrinfo.bus.path.getstationbyname}") String pathOfGetStationByNameList
    ) {
        this.endpoint = endpoint;
        this.apiKey = apiKey;
        this.pathOfGetStationByNameList = pathOfGetStationByNameList;
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
                                JAXBContext jaxbContext = JAXBContext.newInstance(ServiceResultNormalVoImpl.class);
                                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                                StringReader reader = new StringReader(body);
                                IServiceResult result = (ServiceResultNormalVoImpl) unmarshaller.unmarshal(reader);

                                return Mono.just(result);
                            } catch (JAXBException e) {
                                return Mono.error(new RuntimeException(body));
                            }
                        })
                        .onErrorResume(errorBody -> {
                            try {
                                JAXBContext jaxbContext = JAXBContext.newInstance(ServiceResultErrorVoImpl.class);
                                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                                StringReader reader = new StringReader(errorBody.getMessage());
                                IServiceResult result = (ServiceResultNormalVoImpl) unmarshaller.unmarshal(reader);

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
