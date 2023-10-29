package com.thewayhome.ptis.batch.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParamKey implements Serializable {
    @NotNull
    @Column(name="group_name", length = 20, nullable = false)
    @Size(max=20)
    private String groupName;
    @NotNull
    @Column(name="param_name", length = 20, nullable = false)
    @Size(max=20)
    private String paramName;
}
