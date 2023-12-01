package com.thewayhome.ptis.core.util;

import com.thewayhome.ptis.core.vo.ParamVo;
import com.thewayhome.ptis.core.entity.Param;
import com.thewayhome.ptis.core.entity.ParamKey;
import com.thewayhome.ptis.core.repository.ParamRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ParamEntityVoConverter implements IEntityVoConverter<Param, ParamVo> {
    private final ParamRepository paramRepository;
    @Override
    @NotNull
    public Param toEntity(ParamVo vo, String operatorId) {
        ParamKey id = ParamKey.builder()
                .groupName(vo.getGroupName())
                .paramName(vo.getParamName())
                .build();

        Param entity = paramRepository.findById(id)
                .orElse(
                        Param.builder()
                                .id(id)
                                .createdAt(LocalDateTime.now())
                                .createdBy(operatorId)
                                .build()
                );

        if (vo.getValue() != null) entity.setValue(vo.getValue());
        if (vo.getUseYn() != null) entity.setUseYn(vo.getUseYn());

        entity.setUpdatedAt(LocalDateTime.now());
        entity.setUpdatedBy(operatorId);

        return entity;
    }

    @Override
    @NotNull
    public ParamVo toVo(Param entity) {
        return ParamVo.builder()
                .groupName(entity.getId().getGroupName())
                .paramName(entity.getId().getParamName())
                .value(entity.getValue())
                .useYn(entity.getUseYn())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .build();
    }
}
