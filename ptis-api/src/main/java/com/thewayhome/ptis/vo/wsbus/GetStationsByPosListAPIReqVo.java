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
public class GetStationsByPosListAPIReqVo implements Serializable {
    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 12, message = "기준위치 X좌표는 12자까지 입력 가능 합니다.")
    private String tmX;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 12, message = "기준위치 Y좌표는 12자까지 입력 가능 합니다.")
    private String tmY;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 3, message = "검색 반경은 3자까지 입력 가능 합니다.")
    private String radius;
}