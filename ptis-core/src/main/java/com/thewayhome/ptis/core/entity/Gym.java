package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Gym extends BaseEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @Column(name="gym_id", nullable = false)
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
