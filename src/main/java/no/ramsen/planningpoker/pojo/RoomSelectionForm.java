package no.ramsen.planningpoker.pojo;

import javax.validation.constraints.NotBlank;

public class RoomSelectionForm {
    @NotBlank
    private String name;
    @NotBlank
    private String room;

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

    public RoomSelectionForm(String name, String room) {
        this.name = name;
        this.room = room;
    }
}
