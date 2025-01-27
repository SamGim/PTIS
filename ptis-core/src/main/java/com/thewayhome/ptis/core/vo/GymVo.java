package com.thewayhome.ptis.core.vo;

import com.thewayhome.ptis.core.vo.base.BaseVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class GymVo extends BaseVo {
    private String id;
    private String gymName;
    private String gymId;
    private String gymPosX;
    private String gymPosY;
    private String gymAddress;
}
