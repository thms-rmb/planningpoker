package no.ramsen.planningpoker.pojo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record Vote(
        @NotBlank String sessionId,
        @NotBlank String room,
        @NotBlank String name,
        @NotBlank String vote,
        @NotNull boolean ready
) { }
