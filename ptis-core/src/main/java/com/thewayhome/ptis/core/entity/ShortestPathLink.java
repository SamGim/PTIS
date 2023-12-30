package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Entity
@Table(
        name = "ShortestPathLink",
        indexes = {
                @Index(name = "ShortestPathLink_U1", columnList = "id"),
                @Index(name = "ShortestPathLink_X1", columnList = "st_node_id, ed_node_id")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class ShortestPathLink extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private Long id;

    @Column(name = "st_node_id")
    private String stNodeId;

    @Column(name = "ed_node_id")
    private String edNodeId;

    @Column(name="cost", nullable = false)
    private Long cost;

    @Column(name = "prev_node_id")
    private String prevNodeId;

    @Column(name = "link_id")
    private String linkId;
}
