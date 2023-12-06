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
        name = "Gym",
        indexes = {
                @Index(name = "Gym_U1", columnList = "id", unique = true)
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class Gym extends BaseEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @Column(name="gym_id", nullable = false, unique = true)
    private String gymId;

    @Column(name="gym_name", nullable = false)
    private String gymName;

    @Column(name="gym_address", nullable = true)
    private String gymAddress;

    @Column(name="gym_pos_x", nullable = true)
    private String gymPosX;

    @Column(name="gym_pos_y", nullable = true)
    private String gymPosY;

}
