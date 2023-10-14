package com.thewayhome.ptis.vo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@Builder
@ToString
public class RouteFindingAPIReqVo implements Serializable {
    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 12, message = "출발점 기준위치 X좌표는 12자까지 입력 가능 합니다.")
    private String stTmX;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 12, message = "출발점 기준위치 Y좌표는 12자까지 입력 가능 합니다.")
    private String stTmY;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 9, message = "출발점 검색 반경은 9자까지 입력 가능 합니다.")
    private String stRadius;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 12, message = "도착점 기준위치 X좌표는 12자까지 입력 가능 합니다.")
    private String edTmX;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 12, message = "도착점 기준위치 Y좌표는 12자까지 입력 가능 합니다.")
    private String edTmY;

    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 9, message = "도착점 검색 반경은 9자까지 입력 가능 합니다.")
    private String edRadius;
}