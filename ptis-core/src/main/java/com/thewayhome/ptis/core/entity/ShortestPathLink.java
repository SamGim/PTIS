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
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @ManyToOne
    @JoinColumn(name = "st_node_id")
    private Node stNode;

    @ManyToOne
    @JoinColumn(name = "ed_node_id")
    private Node edNode;

    @Column(name="cost", nullable = false)
    private Long cost;

    @ManyToOne
    @JoinColumn(name = "prev_node_id")
    private Node prevNode;
}
