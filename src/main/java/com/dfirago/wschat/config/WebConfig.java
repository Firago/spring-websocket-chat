package com.dfirago.wschat.config;

import com.dfirago.wschat.event.SessionEventListener;
import com.dfirago.wschat.participant.ParticipantRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * @author dmfi
 */
@Configuration
public class WebConfig {

    public static class Destinations {

        private static final String LOGIN_DESTINATION = "/topic/chat.login";
        private static final String LOGOUT_DESTINATION = "/topic/chat.logout";
    }

    @Bean
    @Description("Stores connected users")
    public ParticipantRepository partcipantRepository() {
        return new ParticipantRepository();
    }

    @Bean
    @Description("Tracks participant sessions (connect / disconnect")
    public SessionEventListener sessionEventListener(ParticipantRepository participantRepository, SimpMessagingTemplate messagingTemplate) {
        SessionEventListener listener = new SessionEventListener(participantRepository, messagingTemplate);
        listener.setLoginDestination(Destinations.LOGIN_DESTINATION);
        listener.setLogoutDestination(Destinations.LOGOUT_DESTINATION);
        return listener;
    }

}
