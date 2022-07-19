package no.ramsen.planningpoker.pojo;

public class RevealedForm {
    private boolean revealed;
    private String room;

    public boolean isRevealed() {
        return revealed;
    }

    public String getRoom() {
        return room;
    }

    public RevealedForm(boolean revealed, String room) {
        this.revealed = revealed;
        this.room = room;
    }
}
