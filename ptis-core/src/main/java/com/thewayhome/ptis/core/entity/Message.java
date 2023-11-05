package com.thewayhome.ptis.core.entity;

import com.fasterxml.jackson.databind.JsonNode;
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
public class Message extends BaseEntity {
    @Id
    @Column(name="id", length = 12, nullable = false)
    @Size(min = 12, max = 12)
    private String id;

    @Column(name="msg_header", columnDefinition = "LONGTEXT")
    private JsonNode msgHeader;

    @Column(name="msg_body", columnDefinition = "LONGTEXT")
    private JsonNode msgBody;

    @Column(name="raw_message", columnDefinition = "LONGTEXT")
    private String rawMessage;
}
