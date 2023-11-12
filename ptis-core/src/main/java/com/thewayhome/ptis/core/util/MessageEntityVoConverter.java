package com.thewayhome.ptis.core.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.core.vo.MessageVo;
import com.thewayhome.ptis.core.entity.Message;
import com.thewayhome.ptis.core.repository.MessageRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MessageEntityVoConverter implements IEntityVoConverter<Message, MessageVo> {
    private final MessageRepository messageRepository;
    private final ObjectMapper objectMapper;

    @Override
    @NotNull
    public Message toEntity(MessageVo vo, String operatorId) {
        String rawMessage = vo.getRawMessage();
        JsonNode rootNode = null;
        JsonNode msgHeader = null;
        JsonNode msgBody = null;

        try {
            rootNode = objectMapper.readTree(rawMessage);
            msgHeader = rootNode.get("msgHeader");
            msgBody = rootNode.get("msgBody");

            if (msgHeader.isNull()) throw new IllegalArgumentException();
            if (msgBody.isNull()) throw new IllegalArgumentException();

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid message format");
        }

        Message entity = messageRepository.findById(vo.getId())
                .orElse(
                        Message.builder()
                                .id(vo.getId())
                                .msgHeader(msgHeader)
                                .msgBody(msgBody)
                                .rawMessage(rawMessage)
                                .createdAt(LocalDateTime.now())
                                .createdBy(vo.getOperatorId())
                                .build()
                );

        entity.setMsgHeader(msgHeader.isNull() ? entity.getMsgHeader() : msgHeader);
        entity.setMsgBody(msgBody.isNull() ? entity.getMsgBody() : msgBody);
        entity.setRawMessage(rawMessage.isEmpty() ? entity.getRawMessage() : rawMessage);

        entity.setCreatedAt(entity.getCreatedAt() == null ? LocalDateTime.now() : entity.getCreatedAt());
        entity.setCreatedBy(entity.getCreatedBy() == null ? vo.getOperatorId() : entity.getCreatedBy());
        entity.setUpdatedAt(entity.getUpdatedAt() == null ? LocalDateTime.now() : entity.getUpdatedAt());
        entity.setUpdatedBy(entity.getUpdatedBy() == null ? vo.getOperatorId() : entity.getUpdatedBy());

        return entity;
    }

    @Override
    @NotNull
    public MessageVo toVo(Message entity, String operatorId) {
        return MessageVo.builder()
                .id(entity.getId())
                .msgHeader(entity.getMsgHeader().toPrettyString())
                .msgBody(entity.getMsgBody().toPrettyString())
                .rawMessage(entity.getRawMessage())
                .createdAt(entity.getCreatedAt())
                .createdBy(entity.getCreatedBy())
                .updatedAt(entity.getUpdatedAt())
                .updatedBy(entity.getUpdatedBy())
                .operatorId(operatorId)
                .build();
    }
}
