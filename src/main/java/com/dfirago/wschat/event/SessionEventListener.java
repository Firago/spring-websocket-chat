package com.dfirago.wschat.event;

import com.dfirago.wschat.participant.Participant;
import com.dfirago.wschat.participant.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * @author dmfi
 */
@Component
public class SessionEventListener {

    private final ParticipantRepository participantRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private String loginDestination;

    private String logoutDestination;

    @Autowired
    public SessionEventListener(ParticipantRepository participantRepository, SimpMessagingTemplate messagingTemplate) {
        this.participantRepository = participantRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
        //create participant from given headers
        Participant participant = new Participant(headers.getUser().getName());
        //create participant login event to broadcast it on chat
        LoginEvent loginEvent = new LoginEvent(participant);
        //send login event to destination
        messagingTemplate.convertAndSend(loginDestination, loginEvent);
        //store session for future processing
        participantRepository.add(headers.getSessionId(), participant);
    }
    
    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        Participant participant = participantRepository.getParticipant(sessionId);
        if (participant != null) {
            LogoutEvent logoutEvent = new LogoutEvent(participant);
            messagingTemplate.convertAndSend(logoutDestination, logoutEvent);
            participantRepository.removeParticipant(sessionId);
        }
    }

    public void setLoginDestination(String loginDestination) {
        this.loginDestination = loginDestination;
    }

    public void setLogoutDestination(String logoutDestination) {
        this.logoutDestination = logoutDestination;
    }

}
