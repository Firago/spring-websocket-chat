package com.dfirago.wschat.participant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

/**
 * @author dmfi
 */
@Repository
public class ParticipantRepository {

    private Map<String, Participant> activeSessions = new ConcurrentHashMap<>();

    public void add(String sessionId, Participant participant) {
        activeSessions.put(sessionId, participant);
    }

    public Participant getParticipant(String sessionId) {
        return activeSessions.get(sessionId);
    }

    public void removeParticipant(String sessionId) {
        activeSessions.remove(sessionId);
    }

    public Map<String, Participant> getActiveSessions() {
        return activeSessions;
    }

    public void setActiveSessions(Map<String, Participant> activeSessions) {
        this.activeSessions = activeSessions;
    }

}
