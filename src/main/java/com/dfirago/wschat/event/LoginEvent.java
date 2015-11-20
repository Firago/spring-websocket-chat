package com.dfirago.wschat.event;

import com.dfirago.wschat.participant.Participant;
import java.util.Date;

/**
 * @author dmfi
 */
public class LoginEvent {

    private Participant target;
    private Date time;

    public LoginEvent(Participant target) {
        this.target = target;
        this.time = new Date();
        
    }

    public Participant getTarget() {
        return target;
    }

    public void setTarget(Participant target) {
        this.target = target;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

}
