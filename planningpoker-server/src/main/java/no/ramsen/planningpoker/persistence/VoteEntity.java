package no.ramsen.planningpoker.persistence;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "vote")
public class VoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(optional = false)
    @Column(nullable = false)
    private RoomEntity roomEntity;

    @Column(nullable = false)
    private String participant;

    private String amount;

    @Column(nullable = false)
    private Boolean ready;

    @Column(nullable = false)
    private ZonedDateTime modificationDateTime;

    public VoteEntity() {}

    public VoteEntity(RoomEntity roomEntity, String participant, String amount, Boolean ready, ZonedDateTime modificationDateTime) {
        this.roomEntity = roomEntity;
        this.participant = participant;
        this.amount = amount;
        this.ready = ready;
        this.modificationDateTime = modificationDateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoomEntity getRoomEntity() {
        return roomEntity;
    }

    public void setRoomEntity(RoomEntity roomEntity) {
        this.roomEntity = roomEntity;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String vote) {
        this.amount = vote;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public ZonedDateTime getModificationDateTime() {
        return modificationDateTime;
    }

    public void setModificationDateTime(ZonedDateTime modificationDateTime) {
        this.modificationDateTime = modificationDateTime;
    }
}
