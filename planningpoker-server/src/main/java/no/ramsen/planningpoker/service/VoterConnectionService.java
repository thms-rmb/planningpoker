package no.ramsen.planningpoker.service;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import no.ramsen.planningpoker.pojo.RevealState;

@Service
public class VoterConnectionService {
    private final Logger logger = LoggerFactory.getLogger(VoterConnectionService.class);
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final SpringTemplateEngine springTemplateEngine;

    private final VotingService votingService;
    private final List<String> voteOptions;

    public VoterConnectionService(SimpMessagingTemplate simpMessagingTemplate, SpringTemplateEngine springTemplateEngine, VotingService votingService, @Value("${planningpoker.vote-options}") List<String> voteOptions) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.springTemplateEngine = springTemplateEngine;
        this.votingService = votingService;
        this.voteOptions = voteOptions;
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        var principal = event.getUser();
        if (principal == null)
            return;
        var userName = principal.getName();
        if (!this.votingService.isSessionDefined(userName))
            return;

        var vote = this.votingService.getVoteBySession(userName);
        var room = vote.room();
        this.votingService.removeSession(userName);
        this.updateRoom(room);
    }

    public void updateRoom(String room) {
        var votes = this.votingService.getVotesByRoom(room);
        var revealState = this.votingService.getRoomReveal(room);
        for (var vote : votes) {
            var sessionId = vote.sessionId();
            var context = new Context();
            context.setVariable("votes", votes);
            context.setVariable("sessionId", sessionId);
            context.setVariable("revealed", revealState == RevealState.REVEALED);
            context.setVariable("voteOptions", this.voteOptions);
            var results = this.springTemplateEngine.process("index", Set.of("#room-votes"), context);
            logger.info("Going to send to {}:\n\n{}", sessionId, results);
            this.simpMessagingTemplate.convertAndSendToUser(sessionId, String.format("/room/%s/votes", room), results);
        }
    }
}
