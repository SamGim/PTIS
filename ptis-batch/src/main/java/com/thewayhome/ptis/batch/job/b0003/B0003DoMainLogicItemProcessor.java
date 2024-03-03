package com.thewayhome.ptis.batch.job.b0003;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.core.dto.request.BusRouteAddCourseItemRequestDto;
import com.thewayhome.ptis.core.dto.request.BusRouteProcessRegisterRequestDto;
import com.thewayhome.ptis.core.dto.request.BusStationRegisterRequestDto;
import com.thewayhome.ptis.core.entity.BusStation;
import com.thewayhome.ptis.core.entity.Param;
import com.thewayhome.ptis.core.service.BusStationService;
import com.thewayhome.ptis.core.service.ParamService;
import com.thewayhome.ptis.core.util.APIConnector;
import com.thewayhome.ptis.core.vo.BusRouteVo;
import com.thewayhome.ptis.core.vo.BusStationVo;
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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private int skipFlag = 0;

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

        String dataFromAPI = APIConnector.getDataFromAPI(endpoint, path, queryParams).doOnError(
                e -> {
                    log.error("Error occurred while getting data from API. Jobname: [" + jobName + "], BusRouteId: [" + busRouteId + "], Error: [" + e.getMessage() + "]");
                    skipFlag = 1;
                }
        ).block();

        if (dataFromAPI == null) {
            return null;
        } else if (skipFlag == 1) {
            return null;
        }

        BusRouteProcessRegisterRequestDto routeProcessReq = BusRouteProcessRegisterRequestDto.builder()
                .id(id)
                .busStationLastGatheringDate(this.jobDate)
                .busStationGatheringStatusCode("01")
                .operatorId(jobName)
                .build();

        List<BusStationRegisterRequestDto> stationReqList = new ArrayList<>();
        List<BusRouteAddCourseItemRequestDto> routeAddItemReqList = new ArrayList<>();
        try {
            JsonNode rootNode = objectMapper.readTree(dataFromAPI);

            for (int i = 0; i < rootNode.size(); i++) {
                JsonNode item = rootNode.get(i);
                JsonNode nextItem = i+1 < rootNode.size() ? rootNode.get(i+1) : null;

                String busStationId = item.get("station").asText();
                String busStationNo = item.get("arsId").asText();
                String busStationName = item.get("stationNm").asText();
                String busStationPosX = item.get("gpsX").asText();
                String busStationPosY = item.get("gpsY").asText();
                String busStationBeginTm = Objects.isNull(nextItem) ? null : nextItem.get("beginTm").asText();
                String busStationLastTm = Objects.isNull(nextItem) ? null : nextItem.get("lastTm").asText();

//                BusStationRegisterRequestDto req = BusStationRegisterRequestDto.builder()
//                        .busStationId(busStationId)
//                        .busStationNo(busStationNo)
//                        .busStationName(busStationName)
//                        .busStationPosX(busStationPosX)
//                        .busStationPosY(busStationPosY)
//                        .operatorId(jobName)
//                        .build();
//
//                stationReqList.add(req);

                Optional<BusStation> byArsId = busStationService.findByBusStationId(busStationId);
                if (byArsId.isEmpty()) {
                    continue;
                }

                BusRouteAddCourseItemRequestDto courseReq = BusRouteAddCourseItemRequestDto.builder()
                        .busRoute(BusRouteVo.builder().id(busRouteId).build())
                        .busStation(BusStationVo.builder().id(busStationId).build())
                        .firstBusTime(
                                Objects.isNull(busStationBeginTm) || ":".equals(busStationLastTm) ?
                                        null :
                                        LocalTime.parse(busStationBeginTm, DateTimeFormatter.ofPattern("HH:mm"))
                        )
                        .lastBusTime(
                                Objects.isNull(busStationLastTm) || ":".equals(busStationLastTm) ?
                                        null :
                                        LocalTime.parse(busStationLastTm, DateTimeFormatter.ofPattern("HH:mm"))
                        )
                        .operatorId(jobName)
                        .build();

                routeAddItemReqList.add(courseReq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return B0003DoMainLogicItemOutput.builder()
                .id(id)
                .arsId(busRouteId)
                .busRouteProcessRegisterRequestDto(routeProcessReq)
                .busStationRegisterRequestDtoList(stationReqList)
                .busRouteAddCourseItemRequestDtoList(routeAddItemReqList)
                .build();
    }
}
