package com.thewayhome.ptis.batch.job.b0002;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.core.service.ParamService;
import com.thewayhome.ptis.batch.util.APIConnector;
import com.thewayhome.ptis.core.entity.Param;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.dto.BusRouteRegisterReqDto;
import com.thewayhome.ptis.core.dto.BusStationProcessRegisterReqDto;
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
@Qualifier("B0002DoMainLogicItemProcessor")
@StepScope
public class B0002DoMainLogicItemProcessor implements ItemProcessor<B0002DoMainLogicItemInput, B0002DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private ParamService paramService;
    private ObjectMapper objectMapper;
    private BusStationService busStationService;
    public B0002DoMainLogicItemProcessor(
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
    public B0002DoMainLogicItemOutput process(B0002DoMainLogicItemInput input) throws Exception {
        if (this.stepExecution != null && this.stepExecution.isTerminateOnly()) {
            throw new JobInterruptedException("Job is stopping");
        }

        final String id = input.getId();
        final String arsId = input.getArsId();

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
        queryParams.add("arsId", arsId);

        String dataFromAPI = APIConnector.getDataFromAPI(endpoint, path, queryParams).block();

        if (dataFromAPI == null) {
            return null;
        }

        BusStationProcessRegisterReqDto stationProcessReq = new BusStationProcessRegisterReqDto();
        stationProcessReq.setId(id);
        stationProcessReq.setGatheringStatusCode("02");
        stationProcessReq.setSelfGatheringStatusCode("00");
        stationProcessReq.setRouteGatheringStatusCode("02");
        stationProcessReq.setOperatorId(jobName);

        List<BusRouteRegisterReqDto> routeReqList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(dataFromAPI);

            for (JsonNode item : rootNode) {
                String busRouteAbrv = item.get("busRouteAbrv").asText();
                String busRouteId = item.get("busRouteId").asText();
                String busRouteNm = item.get("busRouteNm").asText();

                BusRouteRegisterReqDto req = new BusRouteRegisterReqDto();
                req.setBusRouteId(busRouteId);
                req.setOperatorId(jobName);
                req.setBusRouteName(busRouteNm);
                req.setBusRouteNo(busRouteAbrv);
                req.setBusRouteSubNo(busRouteNm.substring(busRouteAbrv.length()));

                routeReqList.add(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return B0002DoMainLogicItemOutput.builder()
                .id(id)
                .arsId(arsId)
                .busStationProcessRegisterReqVo(stationProcessReq)
                .busRouteRegisterReqVoList(routeReqList)
                .build();
    }
}
