package com.thewayhome.ptis.core.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class IdSequence {
    @Id
    @Column(name="entity_name", nullable = false)
    private String entityName;

    @Column(name="next_id", nullable = false)
    private Long nextId;
}
