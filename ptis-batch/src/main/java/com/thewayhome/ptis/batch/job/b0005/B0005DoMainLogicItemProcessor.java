package com.thewayhome.ptis.batch.job.b0005;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.core.service.ParamService;
import com.thewayhome.ptis.batch.util.APIConnector;
import com.thewayhome.ptis.core.entity.Param;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.dto.request.BusRouteProcessRegisterRequestDto;
import com.thewayhome.ptis.core.dto.request.BusStationRegisterRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobInterruptedException;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Qualifier("B0005DoMainLogicItemProcessor")
@StepScope
public class B0005DoMainLogicItemProcessor implements ItemProcessor<B0005DoMainLogicItemInput, B0005DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private ParamService paramService;
    private ObjectMapper objectMapper;
    private BusStationService busStationService;
    public B0005DoMainLogicItemProcessor(
            @Value("#{jobParameters[jobName]}") String jobName,
            @Value("#{jobParameters[jobDate]}") String jobDate,
            ParamService paramService,
            ObjectMapper objectMapper,
            BusStationService busStationService
    ) {
        this.jobName = jobName;
        this.jobDate = jobDate;
        this.paramService = paramService;
        this.objectMapper = objectMapper;
        this.busStationService = busStationService;
    }

    @BeforeStep
    public void saveStepExecution(StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }
    @Override
    public B0005DoMainLogicItemOutput process(B0005DoMainLogicItemInput input) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        final String id = input.getId();
        final String busRouteId = input.getBusRouteId();

        Optional<Param> jobOptional = paramService.getBatchJobInputParam(jobName);
        if (jobOptional.isEmpty()) {
            throw new NoSuchJobException("No such Job Param exists. Jobname: [" + jobName + "]");
        }

        Param param = jobOptional.get();
        String[] paramList = param.getValue().split("\\|");
        int paramIdx = 0;

        String endpoint = paramList[paramIdx++];
        String path = paramList[paramIdx++];

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("busRouteId", busRouteId);

        String dataFromAPI = APIConnector.getDataFromAPI(endpoint, path, queryParams).block();

        if (dataFromAPI == null) {
            throw new IllegalArgumentException();
        }

        BusRouteProcessRegisterRequestDto routeProcessReq = BusRouteProcessRegisterRequestDto.builder()
                .id(id)
                .busRouteLastGatheringDate(this.jobDate)
                .busRouteGatheringStatusCode("02")
                .operatorId(jobName)
                .build();

        List<BusStationRegisterRequestDto> stationReqList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(dataFromAPI);

            for (JsonNode item : rootNode) {
                String busStationId = item.get("arsId").asText();
                String busStationNo = item.get("station").asText();
                String busStationName = item.get("stationNm").asText();
                String busStationPosX = item.get("gpsX").asText();
                String busStationPosY = item.get("gpsY").asText();

                BusStationRegisterRequestDto req = BusStationRegisterRequestDto.builder()
                        .busStationId(busStationId)
                        .busStationNo(busStationNo)
                        .busStationName(busStationName)
                        .busStationPosX(busStationPosX)
                        .busStationPosY(busStationPosY)
                        .operatorId(jobName)
                        .build();

                stationReqList.add(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return B0005DoMainLogicItemOutput.builder()
                .id(id)
                .arsId(busRouteId)
                .busRouteProcessRegisterRequestDto(routeProcessReq)
                .busStationRegisterRequestDtoList(stationReqList)
                .build();
    }
}
