package com.thewayhome.ptis.batch.job.b0002;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class B0002DoMainLogicItemOutput {
    private String arsId;
    private String message;
    //private List<B0002DoMainLogicItemOutputSub> busRouteList;
}
