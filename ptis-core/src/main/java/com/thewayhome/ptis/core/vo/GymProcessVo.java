package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GymProcessVo extends BaseVo {
    private String id;
    private GymVo gym;
    private String gymGatheringStatusCode;
    private String gymFirstGatheringDate;
    private String gymLastGatheringDate;
    private String nodeCreationStatusCode;
    private String nodeFirstCreationDate;
    private String nodeLastCreationDate;
    private NodeVo node;
}
