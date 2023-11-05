package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(
        name = "Node",
        indexes = {
                @Index(name = "Node_U1", columnList = "id")
        }
)
public class Node extends BaseEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @Column(name="node_name", nullable = false)
    @Size(max=10)
    private String nodeName;

    @Column(name="node_pos_x", nullable = false)
    @Min(value=0)
    @Max(value=360)
    private double nodePosX;

    @Column(name="node_pos_y", nullable = false)
    @Min(value=0)
    @Max(value=360)
    private double nodePosY;

}
