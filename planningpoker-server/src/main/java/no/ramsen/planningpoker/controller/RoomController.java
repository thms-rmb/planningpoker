package no.ramsen.planningpoker.controller;

import no.ramsen.planningpoker.pojo.*;
import no.ramsen.planningpoker.service.VoterConnectionService;
import no.ramsen.planningpoker.service.VotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
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
                        "votes", Map.of(),
                        "revealed", false
                )
        );
    }

    @MessageMapping("/room-selection")
    public void roomSelection(@Valid RoomSelectionForm roomSelectionForm, Principal principal) {
        this.logger.info(principal.getName());
        var room = roomSelectionForm.room();
        if (!this.votingService.isRoomDefined(room)) {
            this.votingService.putRoomReveal(room, RevealState.HIDDEN);
        }
        var vote = new Vote(principal.getName(), room, roomSelectionForm.name(), "", false);
        this.votingService.putSessionVote(principal.getName(), vote);
        this.voterConnectionService.updateRoom(room);
    }

    @MessageMapping("/room-vote")
    public void roomVote(@Valid Vote vote, Principal principal) {
        this.votingService.putSessionVote(principal.getName(), vote);
        this.voterConnectionService.updateRoom(vote.room());
    }

    @MessageMapping("/reveal-change")
    public void revealChange(@Valid RevealedForm revealedForm) {
        var room = revealedForm.room();
        this.votingService.putRoomReveal(
                room,
                revealedForm.revealed() ? RevealState.REVEALED : RevealState.HIDDEN
        );
        this.voterConnectionService.updateRoom(room);
    }

    @MessageMapping("/room-reset")
    public void roomReset(@Valid RoomForm roomForm) {
        var room = roomForm.room();
        this.votingService.resetRoom(room);
        this.voterConnectionService.updateRoom(room);
    }
}
