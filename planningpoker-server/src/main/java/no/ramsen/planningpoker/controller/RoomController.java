package no.ramsen.planningpoker.controller;

import no.ramsen.planningpoker.persistence.RoomEntity;
import no.ramsen.planningpoker.persistence.RoomRepository;
import no.ramsen.planningpoker.persistence.VoteEntity;
import no.ramsen.planningpoker.persistence.VoteRepository;
import no.ramsen.planningpoker.pojo.*;
import no.ramsen.planningpoker.resource.RoomAdmittance;
import no.ramsen.planningpoker.resource.RoomEntry;
import no.ramsen.planningpoker.service.VoterConnectionService;
import no.ramsen.planningpoker.service.VotingService;
import no.ramsen.planningpoker.vote.VoteAmounts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    private final Logger logger = LoggerFactory.getLogger(RoomController.class);
    private final VoterConnectionService voterConnectionService;
    private final VotingService votingService;
    private final RoomRepository roomRepository;
    private final VoteRepository voteRepository;

    public RoomController(VoterConnectionService voterConnectionService, VotingService votingService, RoomRepository roomRepository, VoteRepository voteRepository) {
        this.voterConnectionService = voterConnectionService;
        this.votingService = votingService;
        this.roomRepository = roomRepository;
        this.voteRepository = voteRepository;
    }

    @PostMapping("/entry")
    public RoomAdmittance entry(@RequestBody @Valid RoomEntry roomEntry) {
        var now = ZonedDateTime.now();

        var room = this.roomRepository
                .findById(roomEntry.getRoomName())
                .orElse(new RoomEntity(roomEntry.getRoomName(), List.of(), false, now));
        roomRepository.save(room);

        var vote = new VoteEntity(room, roomEntry.getParticipant(), VoteAmounts.ZERO.getValue(), false, now);
        voteRepository.save(vote);

        var voteUri = UriComponentsBuilder.fromUriString("/votes/{voteId}")
                .build(vote.getId());
        var updatesUri = UriComponentsBuilder
                .fromUriString("/topic/rooms/{roomId}/participants/{participant}")
                .build(vote.getParticipant());
        return new RoomAdmittance(voteUri, updatesUri);
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
