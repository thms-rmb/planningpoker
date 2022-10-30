package no.ramsen.planningpoker.resource;

import javax.validation.constraints.NotBlank;

public class RoomEntry {
    @NotBlank
    private String participant;

    @NotBlank
    private String roomName;

    public RoomEntry() {}

    public RoomEntry(String participant, String roomName) {
        this.participant = participant;
        this.roomName = roomName;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}
