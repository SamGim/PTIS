package com.thewayhome.ptis.core.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.MessageRepository;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private IdSequenceRepository idSequenceRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public Message saveMessage(
            String rawMessage,
            String operatorId
    ) {
        Message message = new Message();

        try {
            JsonNode rootNode = objectMapper.readTree(rawMessage);
            JsonNode msgHeader = rootNode.get("msgHeader");
            JsonNode msgBody = rootNode.get("msgBody");

            if (msgHeader.isNull()) throw new IllegalArgumentException();
            if (msgBody.isNull()) throw new IllegalArgumentException();

            message.setMsgHeader(msgHeader);
            message.setMsgBody(msgBody);

        } catch (Exception e) {
            message.setRawMessage(rawMessage);
        }

        // ID
        IdSequence idSequence = idSequenceRepository.findById("MESSAGE")
                .orElse(new IdSequence("MESSAGE", 0L));
        Long nextId = idSequence.getNextId();

        idSequence.setNextId(nextId + 1);
        idSequenceRepository.save(idSequence);

        message.setId(String.format("%012d", nextId + 1));

        // DB
        message.setCreatedAt(LocalDateTime.now());
        message.setCreatedBy(operatorId);
        message.setUpdatedAt(LocalDateTime.now());
        message.setUpdatedBy(operatorId);

        return messageRepository.saveAndFlush(message);
    }
}
