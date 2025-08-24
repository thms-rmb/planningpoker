package no.ramsen.planningpoker.pojo;

import jakarta.validation.constraints.NotBlank;

public record RoomSelectionForm(
        @NotBlank String name,
        @NotBlank String room
) { }
