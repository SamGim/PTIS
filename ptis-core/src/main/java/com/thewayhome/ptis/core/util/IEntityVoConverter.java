package com.thewayhome.ptis.core.util;

import jakarta.validation.constraints.NotNull;

public interface IEntityVoConverter<E, V> {
    @NotNull E toEntity(V dto, String operatorId);
    @NotNull V toVo(E entity, String operatorId);
}
