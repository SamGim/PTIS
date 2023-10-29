package com.thewayhome.ptis.batch.service;

import com.thewayhome.ptis.batch.repository.ParamRepository;
import com.thewayhome.ptis.batch.vo.Param;
import com.thewayhome.ptis.batch.vo.ParamKey;
import com.thewayhome.ptis.batch.vo.ParamsRegisterReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ParamService {
    public static final String BATCH_JOB_INPUT_PARAM_GROUP_NAME = "JOB_INPUT_PARAM";
    @Autowired
    private ParamRepository paramRepository;

    public Optional<Param> getBatchJobInputParam(String jobName) {
        // ID
        ParamKey id = ParamKey.builder()
                .groupName(BATCH_JOB_INPUT_PARAM_GROUP_NAME)
                .paramName(jobName)
                .build();

        return paramRepository.findById(id);
    }

    public Param saveParam(ParamsRegisterReqVo req) {
        // ID
        ParamKey id = ParamKey.builder()
                .groupName(req.getGroupName())
                .paramName(req.getParamName())
                .build();

        Param param = paramRepository.findById(id).orElse(new Param());

        // DATA
        param.setId(id);
        param.setValue(req.getValue());
        param.setUseYn(req.getUseYn());

        // DB
        if (paramRepository.findById(id).isEmpty()) {
            param.setCreatedAt(LocalDateTime.now());
            param.setCreatedBy(req.getOperatorId());
        }
        param.setUpdatedAt(LocalDateTime.now());
        param.setUpdatedBy(req.getOperatorId());

        return paramRepository.save(param);
    }
}
