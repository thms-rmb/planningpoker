package no.ramsen.planningpoker.service;

import no.ramsen.planningpoker.pojo.RevealState;
import no.ramsen.planningpoker.pojo.Vote;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VotingService {
    private final Map<String, RevealState> roomReveals = new HashMap<>();
    private final Map<String, Vote> sessionVotes = new HashMap<>();

    public void putRoomReveal(String room, RevealState revealState) {
        this.roomReveals.put(room, revealState);
    }

    public RevealState getRoomReveal(String room) {
        return this.roomReveals.get(room);
    }

    public boolean isRoomDefined(String room) {
        return this.roomReveals.containsKey(room);
    }

    public void putSessionVote(String session, Vote vote) {
        this.sessionVotes.put(session, vote);
    }

    public boolean isSessionDefined(String session) {
        return this.sessionVotes.containsKey(session);
    }

    public void removeSession(String session) {
        this.sessionVotes.remove(session);
    }

    public List<Vote> getVotesByRoom(String room) {
        return this.sessionVotes
                .values()
                .stream()
                .filter(vote -> vote.room().equals(room))
                .toList();
    }

    public Vote getVoteBySession(String session) {
        if (this.sessionVotes.containsKey(session))
            return this.sessionVotes.get(session);
        return null;
    }

    public void resetRoom(String room) {
        if (this.roomReveals.containsKey(room)) {
            this.roomReveals.put(room, RevealState.HIDDEN);
        }

        this.sessionVotes.replaceAll((sessionId, vote) -> {
            if (!vote.room().equals(room)) {
                return vote;
            }

            return new Vote(
                    vote.sessionId(), vote.room(), vote.name(), "", false
            );
        });
    }
}
