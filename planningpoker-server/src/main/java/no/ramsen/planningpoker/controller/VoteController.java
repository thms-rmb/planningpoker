package no.ramsen.planningpoker.controller;

import no.ramsen.planningpoker.persistence.VoteEntity;
import no.ramsen.planningpoker.persistence.VoteRepository;
import no.ramsen.planningpoker.resource.AmountResource;
import no.ramsen.planningpoker.resource.VoteResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/votes")
@Validated
public class VoteController {
    private final VoteRepository voteRepository;

    public VoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @GetMapping("/{voteId}")
    @Valid
    public ResponseEntity<VoteResource> getVote(@PathVariable(name = "voteId") VoteEntity vote) {
        var headers = new HttpHeaders();
        headers.set(HttpHeaders.ETAG, this.generateEtag(vote));

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(VoteResource.fromVoteEntity(vote));
    }

    @PutMapping("/{voteId}/amount")
    @Valid
    public ResponseEntity<VoteResource> updateAmount(
            @PathVariable(name = "voteId") VoteEntity vote,
            @RequestBody @Valid AmountResource amount) {
        vote.setAmount(amount.getAmount().getValue());
        vote = voteRepository.save(vote);

        var headers = new HttpHeaders();
        headers.set(HttpHeaders.ETAG, this.generateEtag(vote));

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(VoteResource.fromVoteEntity(vote));
    }

    private String generateEtag(VoteEntity vote) {
        var dateTime = vote.getModificationDateTime().format(DateTimeFormatter.ISO_DATE_TIME);

        return DigestUtils.md5DigestAsHex(dateTime.getBytes(StandardCharsets.UTF_8));
    }
}
