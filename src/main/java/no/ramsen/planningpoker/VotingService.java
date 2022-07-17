package no.ramsen.planningpoker;

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

    public List<Vote> getVotesByRoom(String room) {
        return this.sessionVotes
                .values()
                .stream()
                .filter(vote -> vote.getRoom().equals(room))
                .toList();
    }
}
