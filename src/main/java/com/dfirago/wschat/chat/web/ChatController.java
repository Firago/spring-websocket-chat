package com.dfirago.wschat.chat.web;

import com.dfirago.wschat.chat.ChatMessage;
import com.dfirago.wschat.participant.Participant;
import com.dfirago.wschat.participant.ParticipantRepository;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author dmfi
 */
@Controller
public class ChatController {

    private final ParticipantRepository participantRepository;

    private final SimpMessagingTemplate messagingTemplate;
    
    private static final String SYSTEM = "System";

    @Autowired
    public ChatController(ParticipantRepository participantRepository, SimpMessagingTemplate messagingTemplate) {
        this.participantRepository = participantRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @SubscribeMapping("/chat.participants")
    public Collection<Participant> retrieveParticipant() {
        return participantRepository.getActiveSessions().values();
    }

    @MessageMapping("/chat.message")
    public ChatMessage filterMessage(@Payload ChatMessage message, SimpMessageHeaderAccessor headers) {
        message.setSender(participantRepository.getParticipant(headers.getSessionId()).getUsername());
        return message;
    }

    @MessageMapping("/chat.broadcast")
    public ChatMessage filterBroadcast(@Payload ChatMessage message) {
        message.setSender(SYSTEM);
        return message;
    }

    @MessageMapping("/chat.private.{username}")
    public void filterPrivateMessage(@Payload ChatMessage message, @DestinationVariable("username") String username, SimpMessageHeaderAccessor headers) {
        message.setSender(participantRepository.getParticipant(headers.getSessionId()).getUsername());
        messagingTemplate.convertAndSend(String.format("/user/%s/exchange/amq.direct/chat.message", username), message);
    }

    @ExceptionHandler
    @SendToUser(value = "/exchange/amq.direct/errors", broadcast = false)
    public String handleException(Exception e) {
        return e.getMessage();
    }

}
