package com.thewayhome.ptis.vo.tago;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetRouteInfoItemAPIReqVo {
    @Size(max = 10, message = "데이터 타입은 10자까지 입력 가능 합니다.")
    private String _type;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 5, message = "도시 코드는 5자까지 입력 가능 합니다.")
    private String cityCode;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 30, message = "노선 ID는 30자까지 입력 가능 합니다.")
    private String routeId;
}