package com.thewayhome.ptis.batch.job.b0003;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.core.service.ParamService;
import com.thewayhome.ptis.batch.util.APIConnector;
import com.thewayhome.ptis.core.entity.Param;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.vo.BusRouteProcessRegisterReqVo;
import com.thewayhome.ptis.core.vo.BusStationRegisterReqVo;
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
@Qualifier("B0003DoMainLogicItemProcessor")
@StepScope
public class B0003DoMainLogicItemProcessor implements ItemProcessor<B0003DoMainLogicItemInput, B0003DoMainLogicItemOutput> {
    private final String jobName;
    private final String jobDate;
    private StepExecution stepExecution;
    private ParamService paramService;
    private ObjectMapper objectMapper;
    private BusStationService busStationService;
    public B0003DoMainLogicItemProcessor(
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
    public B0003DoMainLogicItemOutput process(B0003DoMainLogicItemInput input) throws Exception {
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

        BusRouteProcessRegisterReqVo routeProcessReq = new BusRouteProcessRegisterReqVo();
        routeProcessReq.setId(id);
        routeProcessReq.setGatheringStatusCode("02");
        routeProcessReq.setOperatorId(jobName);

        List<BusStationRegisterReqVo> stationReqList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(dataFromAPI);

            for (JsonNode item : rootNode) {
                String busStationId = item.get("arsId").asText();
                String busStationNo = item.get("station").asText();
                String busStationName = item.get("stationNm").asText();
                String busStationPosX = item.get("gpsX").asText();
                String busStationPosY = item.get("gpsY").asText();

                BusStationRegisterReqVo req = new BusStationRegisterReqVo();
                req.setBusStationId(busStationId);
                req.setBusStationNo(busStationNo);
                req.setBusStationName(busStationName);
                req.setBusStationPosX(busStationPosX);
                req.setBusStationPosY(busStationPosY);
                req.setOperatorId(jobName);

                stationReqList.add(req);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return B0003DoMainLogicItemOutput.builder()
                .id(id)
                .arsId(busRouteId)
                .busRouteProcessRegisterReqVo(routeProcessReq)
                .busStationRegisterReqVoList(stationReqList)
                .build();
    }
}
