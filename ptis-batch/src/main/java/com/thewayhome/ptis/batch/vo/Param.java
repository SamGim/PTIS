package com.thewayhome.ptis.batch.vo;

import com.thewayhome.ptis.core.vo.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(
        name = "Param",
        indexes = {
                @Index(name = "Param_U1", columnList = "group_name,param_name")
        }
)
public class Param extends BaseEntity {
    @EmbeddedId
    private ParamKey id;
    @Column(name="value", nullable = false)
    @Size(max=100)
    private String value;
    @Column(name="use_yn", nullable = false)
    @Size(max=1)
    private String useYn;
}
