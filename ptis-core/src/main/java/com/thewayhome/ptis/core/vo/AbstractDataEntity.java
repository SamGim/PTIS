package com.thewayhome.ptis.core.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@MappedSuperclass
public abstract class AbstractDataEntity extends BaseEntity {
    @Column(name = "tp_dscd", nullable = false)
    private String tpDscd;

    @Column(name = "sr_dscd", nullable = false)
    private String srDscd;

    @Column(name = "gs_dscd", nullable = false)
    private String gsDscd;
}