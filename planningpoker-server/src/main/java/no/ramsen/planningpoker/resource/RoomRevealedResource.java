package no.ramsen.planningpoker.resource;

public class RoomRevealedResource {
    private boolean revealed;

    public RoomRevealedResource() {}

    public RoomRevealedResource(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }
}
