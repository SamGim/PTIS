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
        name = "Link",
        indexes = {
                @Index(name = "Link_U1", columnList = "id"),
                @Index(name = "Link_X1", columnList = "st_node_id, ed_node_id"),
                @Index(name = "Link_X2", columnList = "link_type, link_name")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class Link extends BaseEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @Column(name="link_type", nullable = false)
    @Size(max=10)
    private String linkType;

    @Column(name="link_name", nullable = false)
    @Size(max=10)
    private String linkName;

    @ManyToOne
    @JoinColumn(name = "st_node_id")
    private Node stNode;

    @ManyToOne
    @JoinColumn(name = "ed_node_id")
    private Node edNode;

    @Column(name="cost", nullable = false)
    private Long cost;

}
