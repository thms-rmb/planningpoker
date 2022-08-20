package no.ramsen.planningpoker.pojo;

import javax.validation.constraints.NotBlank;

public record RoomSelectionForm(
        @NotBlank String name,
        @NotBlank String room
) { }
