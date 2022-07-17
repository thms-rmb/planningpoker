package no.ramsen.planningpoker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Set;

@Service
public class VoterConnectionService {
    private final Logger logger = LoggerFactory.getLogger(VoterConnectionService.class);
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final SpringTemplateEngine springTemplateEngine;

    private final VotingService votingService;

    public VoterConnectionService(SimpMessagingTemplate simpMessagingTemplate, SpringTemplateEngine springTemplateEngine, VotingService votingService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.springTemplateEngine = springTemplateEngine;
        this.votingService = votingService;
    }

    public void updateRoom(String room) {
        var votes = this.votingService.getVotesByRoom(room);
        var revealState = this.votingService.getRoomReveal(room);
        for (var vote : votes) {
            var sessionId = vote.getSessionId();
            var context = new Context();
            context.setVariable("votes", votes);
            context.setVariable("sessionId", sessionId);
            context.setVariable("revealed", revealState == RevealState.REVEALED);
            var results = this.springTemplateEngine.process("index", Set.of("#room-votes"), context);
            logger.info("Going to send to {}:\n\n{}", sessionId, results);
            this.simpMessagingTemplate.convertAndSendToUser(sessionId, String.format("/room/%s/votes", room), results);
        }
    }
}
