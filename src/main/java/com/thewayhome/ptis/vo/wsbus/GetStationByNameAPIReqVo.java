package com.thewayhome.ptis.vo.wsbus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class GetStationByNameAPIReqVo implements Serializable {
    @NotBlank(message = "필수 입력 값 입니다.")
    @Size(max = 60, message = "정류소명 검색어는 60자까지 입력 가능 합니다.")
    private String stSrch;
}