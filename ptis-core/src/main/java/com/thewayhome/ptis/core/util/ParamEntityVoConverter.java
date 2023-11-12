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
                                .value(vo.getValue())
                                .useYn(vo.getUseYn())
                                .createdAt(LocalDateTime.now())
                                .createdBy(vo.getOperatorId())
                                .build()
                );

        entity.setValue(vo.getValue() == null ? entity.getValue() : vo.getValue());
        entity.setUseYn(vo.getUseYn() == null ? entity.getUseYn() : vo.getUseYn());

        entity.setCreatedAt(entity.getCreatedAt() == null ? LocalDateTime.now() : entity.getCreatedAt());
        entity.setCreatedBy(entity.getCreatedBy() == null ? vo.getOperatorId() : entity.getCreatedBy());
        entity.setUpdatedAt(entity.getUpdatedAt() == null ? LocalDateTime.now() : entity.getUpdatedAt());
        entity.setUpdatedBy(entity.getUpdatedBy() == null ? vo.getOperatorId() : entity.getUpdatedBy());

        return entity;
    }

    @Override
    @NotNull
    public ParamVo toVo(Param entity, String operatorId) {
        return ParamVo.builder()
                .groupName(entity.getId().getGroupName())
                .paramName(entity.getId().getParamName())
                .value(entity.getValue())
                .useYn(entity.getUseYn())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
