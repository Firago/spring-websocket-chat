package com.dfirago.wschat.event;

import com.dfirago.wschat.participant.Participant;

/**
 * @author dmfi
 */
public class LogoutEvent {

    private Participant target;

    public LogoutEvent(Participant target) {
        this.target = target;
    }

    public Participant getTarget() {
        return target;
    }

    public void setTarget(Participant target) {
        this.target = target;
    }

}
