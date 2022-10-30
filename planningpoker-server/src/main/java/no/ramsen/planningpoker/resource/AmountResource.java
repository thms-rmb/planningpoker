package no.ramsen.planningpoker.resource;

import no.ramsen.planningpoker.vote.VoteAmounts;

import javax.validation.constraints.NotNull;

public class AmountResource {
    @NotNull
    private VoteAmounts amount;

    public AmountResource() {}

    public AmountResource(VoteAmounts amount) {
        this.amount = amount;
    }

    public VoteAmounts getAmount() {
        return amount;
    }

    public void setAmount(VoteAmounts amount) {
        this.amount = amount;
    }
}
