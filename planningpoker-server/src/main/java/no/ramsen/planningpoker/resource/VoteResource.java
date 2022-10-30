package no.ramsen.planningpoker.resource;

import no.ramsen.planningpoker.persistence.VoteEntity;
import no.ramsen.planningpoker.vote.VoteAmounts;

import javax.validation.constraints.NotNull;

public class VoteResource extends AmountResource {
    @NotNull
    private Long id;

    @NotNull
    private String room;

    @NotNull
    private String participant;

    @NotNull
    private Boolean ready;

    public VoteResource() {}

    public VoteResource(Long id, String room, String participant, VoteAmounts amount, Boolean ready) {
        this.id = id;
        this.room = room;
        this.participant = participant;
        this.setAmount(amount);
        this.ready = ready;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public static VoteResource fromVoteEntity(VoteEntity vote) {
        return new VoteResource(
                vote.getId(),
                vote.getRoomEntity().getName(),
                vote.getParticipant(),
                switch (vote.getAmount()) {
                    case "½" -> VoteAmounts.HALF;
                    case "1" -> VoteAmounts.ONE;
                    case "2" -> VoteAmounts.TWO;
                    case "3" -> VoteAmounts.THREE;
                    case "5" -> VoteAmounts.FIVE;
                    case "8" -> VoteAmounts.EIGHT;
                    default -> VoteAmounts.ZERO;
                },
                false
        );
    }
}
