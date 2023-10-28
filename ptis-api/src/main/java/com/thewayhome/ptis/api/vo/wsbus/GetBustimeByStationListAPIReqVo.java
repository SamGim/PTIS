package com.thewayhome.ptis.api.vo.wsbus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class GetBustimeByStationListAPIReqVo implements Serializable {
    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 5, message = "정류소 고유 번호는 5자까지 입력 가능 합니다.")
    private String arsId;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 9, message = "노선 ID는 9자까지 입력 가능 합니다.")
    private String busRouteId;
}