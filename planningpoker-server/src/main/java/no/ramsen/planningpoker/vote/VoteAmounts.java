package no.ramsen.planningpoker.vote;


import com.fasterxml.jackson.annotation.JsonValue;

public enum VoteAmounts {
    ZERO ("0"),
    HALF ("½"),
    ONE ("1"),
    TWO ("2"),
    THREE ("3"),
    FIVE ("5"),
    EIGHT ("8");

    private final String value;

    VoteAmounts(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
