package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.dto.request.ParamsRegisterRequestDto;
import com.thewayhome.ptis.core.entity.Param;
import com.thewayhome.ptis.core.entity.ParamKey;
import com.thewayhome.ptis.core.repository.ParamRepository;
import com.thewayhome.ptis.core.util.ParamEntityVoConverter;
import com.thewayhome.ptis.core.vo.ParamVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParamService {
    public static final String BATCH_JOB_INPUT_PARAM_GROUP_NAME = "JOB_INPUT_PARAM";
    public static final String SERVICE_INPUT_PARAM_GROUP_NAME = "SERVICE_INPUT_PARAM";
    private final ParamRepository paramRepository;
    private final ParamEntityVoConverter paramEntityDtoConverter;

    public Optional<Param> findById(ParamKey id) {
        return paramRepository.findById(id);
    }
    public Param saveParam(ParamVo req, String operatorId) {
        Param entity = paramEntityDtoConverter.toEntity(req, operatorId);
        return paramRepository.save(entity);
    }
    public Optional<Param> getBatchJobInputParam(String jobName) {
        // ID
        ParamKey id = ParamKey.builder()
                .groupName(BATCH_JOB_INPUT_PARAM_GROUP_NAME)
                .paramName(jobName)
                .build();

        return paramRepository.findById(id);
    }
    public Param registerParam(ParamsRegisterRequestDto req) {
        // Message
        ParamVo paramVo = ParamVo.builder()
                .groupName(req.getGroupName())
                .paramName(req.getParamName())
                .value(req.getValue())
                .useYn(req.getUseYn())
                .build();

        Param param = this.saveParam(paramVo, req.getOperatorId());

        return param;
    }
}
