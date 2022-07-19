package no.ramsen.planningpoker.controller;

import no.ramsen.planningpoker.pojo.RevealState;
import no.ramsen.planningpoker.pojo.RevealedForm;
import no.ramsen.planningpoker.pojo.RoomSelectionForm;
import no.ramsen.planningpoker.pojo.Vote;
import no.ramsen.planningpoker.service.VoterConnectionService;
import no.ramsen.planningpoker.service.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@Controller
public class RoomController {
    private final Logger logger = LoggerFactory.getLogger(RoomController.class);
    private final VoterConnectionService voterConnectionService;
    private final VotingService votingService;

    public RoomController(VoterConnectionService voterConnectionService, VotingService votingService) {
        this.voterConnectionService = voterConnectionService;
        this.votingService = votingService;
    }

    @GetMapping("/")
    public ModelAndView index(@ModelAttribute RoomSelectionForm roomSelection) {
        return new ModelAndView(
                "index",
                Map.of(
                        "roomSelection", roomSelection,
                        "votes", Map.of()
                )
        );
    }

    @MessageMapping("/room-selection")
    public void roomSelection(@Valid RoomSelectionForm roomSelectionForm, Principal principal) {
        this.logger.info(principal.getName());
        var room = roomSelectionForm.getRoom();
        if (!this.votingService.isRoomDefined(room)) {
            this.votingService.putRoomReveal(room, RevealState.HIDDEN);
        }
        var vote = new Vote(principal.getName(), room, roomSelectionForm.getName(), "");
        this.votingService.putSessionVote(principal.getName(), vote);
        this.voterConnectionService.updateRoom(room);
    }

    @MessageMapping("/room-vote")
    public void roomVote(@Valid Vote vote, Principal principal) {
        this.votingService.putSessionVote(principal.getName(), vote);
        this.voterConnectionService.updateRoom(vote.getRoom());
    }

    @MessageMapping("/reveal-change")
    public void revealChange(@Valid RevealedForm revealedForm) {
        var room = revealedForm.getRoom();
        this.votingService.putRoomReveal(
                room,
                revealedForm.isRevealed() ? RevealState.REVEALED : RevealState.HIDDEN
        );
        this.voterConnectionService.updateRoom(room);
    }
}
