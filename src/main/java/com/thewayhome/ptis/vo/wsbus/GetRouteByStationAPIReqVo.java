package com.thewayhome.ptis.vo.wsbus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class GetRouteByStationAPIReqVo implements Serializable {
    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 5, message = "정류소 고유 번호는 5자까지 입력 가능 합니다.")
    private String arsId;
}