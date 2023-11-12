package com.thewayhome.ptis.core.service;

import com.thewayhome.ptis.core.vo.MessageVo;
import com.thewayhome.ptis.core.dto.request.MessageRegisterRequestDto;
import com.thewayhome.ptis.core.entity.IdSequence;
import com.thewayhome.ptis.core.entity.Message;
import com.thewayhome.ptis.core.repository.IdSequenceRepository;
import com.thewayhome.ptis.core.repository.MessageRepository;
import com.thewayhome.ptis.core.util.MessageEntityVoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final IdSequenceRepository idSequenceRepository;
    private final MessageEntityVoConverter messageEntityDtoConverter;
    public Optional<Message> findById(String id) {
        return messageRepository.findById(id);
    }
    public Message saveMessage(MessageVo req) {
        Message entity = messageEntityDtoConverter.toEntity(req, req.getOperatorId());
        return messageRepository.save(entity);
    }
    public Message registerMessage(MessageRegisterRequestDto req) {
        // ID
        IdSequence idSequence = idSequenceRepository.findById("MESSAGE")
                .orElse(new IdSequence("MESSAGE", 0L));
        Long id = idSequence.getNextId() + 1;

        idSequence.setNextId(id);
        idSequenceRepository.save(idSequence);

        req.setId(String.format("%012d", id));

        // Message
        MessageVo messageVo = MessageVo.builder()
                .id(req.getId())
                .msgHeader(req.getMsgHeader())
                .msgBody(req.getMsgBody())
                .rawMessage(req.getRawMessage())
                .operatorId(req.getOperatorId())
                .build();

        Message message = this.saveMessage(messageVo);

        return message;
    }
}
