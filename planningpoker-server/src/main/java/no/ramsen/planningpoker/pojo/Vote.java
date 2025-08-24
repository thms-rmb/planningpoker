package no.ramsen.planningpoker.pojo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Vote(
        @NotBlank String sessionId,
        @NotBlank String room,
        @NotBlank String name,
        @NotBlank String vote,
        @NotNull boolean ready
) { }
