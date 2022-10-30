package no.ramsen.planningpoker.persistence;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
public class RoomEntity {
    @Id
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteEntity> voteEntities;

    @Column(nullable = false)
    private boolean revealed;

    @Column(nullable = false)
    private ZonedDateTime modificationDateTime;

    public RoomEntity() {}

    public RoomEntity(String name, List<VoteEntity> voteEntities, boolean revealed, ZonedDateTime modificationDateTime) {
        this.name = name;
        this.voteEntities = voteEntities;
        this.revealed = revealed;
        this.modificationDateTime = modificationDateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<VoteEntity> getVoteEntities() {
        return voteEntities;
    }

    public void setVoteEntities(List<VoteEntity> voteEntities) {
        this.voteEntities = voteEntities;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public ZonedDateTime getModificationDateTime() {
        return modificationDateTime;
    }

    public void setModificationDateTime(ZonedDateTime modificationDateTime) {
        this.modificationDateTime = modificationDateTime;
    }
}
