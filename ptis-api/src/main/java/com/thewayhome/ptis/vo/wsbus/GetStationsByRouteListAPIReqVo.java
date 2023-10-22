package com.thewayhome.ptis.vo.wsbus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.Serializable;
import java.lang.reflect.Field;

@Getter
@Setter
@Builder
public class GetStationsByRouteListAPIReqVo implements Serializable {
    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 9, message = "버스 노선ID는 9자까지 입력 가능 합니다.")
    private String busRouteId;
}