package com.thewayhome.ptis.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.thewayhome.ptis.core.dto.response.BusStationQueryResponseDto;
import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.entity.Param;
import com.thewayhome.ptis.core.util.APIConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchService {
    private final ParamService paramService;
    private final BusStationService busStationService;
    private final ObjectMapper objectMapper;
    private final String SERVICE_NAME = "S0001";

    public BusStationQueryResponseDto queryNearestBusStationInfo(String lat, String lng, String radius){
        Optional<Param> jobOptional = paramService.getBatchJobInputParam(SERVICE_NAME);

        Param param = jobOptional.get();
        String[] paramList = param.getValue().split("\\|");
        int paramIdx = 0;

        String endpoint = paramList[paramIdx++];
        String path = paramList[paramIdx++];

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("tmX", lng);
        queryParams.add("tmY", lat);
        queryParams.add("radius", radius);

        log.info("### S0001 :: API 호출 시작 ###");
        String dataFromAPI = APIConnector.getDataFromAPI(endpoint, path, queryParams).block();
        log.info("### S0001 :: API 호출 종료 ###");

        if (dataFromAPI == null) {
            log.info("### S0001 :: API 호출 결과 - 실패 ###");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "API 호출에 실패하였습니다.");
        } else {
            log.info("### S0001 :: API 호출 결과 - 성공 ###");

            try {
                JsonNode rootNode = objectMapper.readTree(dataFromAPI);

                log.info("### S0001 :: 버스노선 내 버스정류장 목록 JSON 파싱 시작 ###");
                for (JsonNode item : rootNode) {
                    String arsId = item.get("arsId").asText();
                    String dist = item.get("dist").asText();
                    String gpsX = item.get("gpsX").asText();
                    String gpsY = item.get("gpsY").asText();
                    String posX = item.get("posX").asText();
                    String posY = item.get("posY").asText();
                    String stationId = item.get("stationId").asText();
                    String stationNm = item.get("stationNm").asText();
                    String stationTp = item.get("stationTp").asText();

                    Optional<BusStation> byStationId = busStationService.findByArsId(stationId);

                    if (byStationId.isEmpty()) {
                        log.info("### S0001 :: DB 내 해당 정류장 정보가 없습니다. ###");
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DB 내 해당 정류장 정보가 없습니다.");
                    }
                    int duration = (int) (Double.parseDouble(dist) / 1000.0 / (4.0 / 60.0));

                    return BusStationQueryResponseDto.builder()
                            .arsId(arsId)
                            .dist(dist)
                            .duration(duration)
                            .gpsX(gpsX)
                            .gpsY(gpsY)
                            .posX(posX)
                            .posY(posY)
                            .stationId(stationId)
                            .stationNm(stationNm)
                            .stationTp(stationTp)
                            .busStationId(byStationId.get().getId())
                            .build();
                }
                log.info("### S0001 :: 버스노선 내 버스정류장 목록 JSON 파싱 종료 ###");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        log.info("### S0001 :: process 종료 ###");

        return null;
    }
}
