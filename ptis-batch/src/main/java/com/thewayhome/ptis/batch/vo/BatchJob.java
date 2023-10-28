package com.thewayhome.ptis.batch.vo;

import com.thewayhome.ptis.core.vo.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class BatchJob extends BaseEntity {
    @Id
    @Column(name="name", length = 5, nullable = false)
    @Size(max=5)
    private String name;
    @Column(name="input_type", nullable = false)
    @Size(max=1)
    private String inputType;
    @Column(name="input_filename", nullable = false)
    @Size(max=100)
    private String inputFilename;
    @Column(name="input_delimiter", nullable = false)
    @Size(max=1)
    private String inputDelimiter;
    @Column(name="use_yn", nullable = false)
    @Size(max=1)
    private String useYn;
}
