package no.ramsen.planningpoker.resource;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RoomResource extends RoomRevealedResource {
    @NotBlank
    private String name;

    @NotNull
    private boolean revealed;


}
