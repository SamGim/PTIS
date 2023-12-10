package com.thewayhome.ptis.core.entity;

import com.thewayhome.ptis.core.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant extends BaseEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @Column(name="restaurant_id", nullable = false)
    private String restaurantId;

    @Column(name="restaurant_name", nullable = false)
    private String restaurantName;

    @Column(name="restaurant_address", nullable = true)
    private String restaurantAddress;

    @Column(name="restaurant_type", nullable = true)
    private String restaurantType;

    @Column(name="restaurant_pos_x", nullable = true)
    private String restaurantPosX;

    @Column(name="restaurant_pos_y", nullable = true)
    private String restaurantPosY;

}
