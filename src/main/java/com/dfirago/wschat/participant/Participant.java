package com.dfirago.wschat.participant;

/**
 * @author dmfi
 */
public class Participant {

    private String username;

    public Participant(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Participant{" + "username=" + username + '}';
    }

}
