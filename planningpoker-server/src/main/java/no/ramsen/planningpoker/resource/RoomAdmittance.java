package no.ramsen.planningpoker.resource;

import java.net.URI;

public class RoomAdmittance {
    private URI vote;
    private URI updates;

    public RoomAdmittance() {}

    public RoomAdmittance(URI vote, URI updates) {
        this.vote = vote;
        this.updates = updates;
    }

    public URI getVote() {
        return vote;
    }

    public void setVote(URI vote) {
        this.vote = vote;
    }

    public URI getUpdates() {
        return updates;
    }

    public void setUpdates(URI updates) {
        this.updates = updates;
    }
}
