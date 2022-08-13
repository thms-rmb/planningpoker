package no.ramsen.planningpoker.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Vote {
    @NotBlank
    private String sessionId;
    @NotBlank
    private String room;
    @NotBlank
    private String name;
    private String vote;
    @NotNull
    private boolean ready;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public Vote(String sessionId, String room, String name, String vote, boolean ready) {
        this.sessionId = sessionId;
        this.room = room;
        this.name = name;
        this.vote = vote;
        this.ready = ready;
    }
}
