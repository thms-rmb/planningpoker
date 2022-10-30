package no.ramsen.planningpoker.vote;

import no.ramsen.planningpoker.persistence.VoteRepository;
import no.ramsen.planningpoker.resource.VoteResource;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

@Component
public class VoteETagValidator implements ConstraintValidator<VoteETagConstraint, VoteResource> {
    private final VoteRepository voteRepository;

    public VoteETagValidator(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
    }

    @Override
    public boolean isValid(VoteResource voteResource, ConstraintValidatorContext constraintValidatorContext) {
        var voteOptional = this.voteRepository.findById(voteResource.getId());
        if (voteOptional.isEmpty()) {
            return true;
        }

        var vote = voteOptional.get();
        var dateTime = vote.getModificationDateTime().format(DateTimeFormatter.ISO_DATE_TIME);
        var expectedETag = DigestUtils.md5DigestAsHex(dateTime.getBytes(StandardCharsets.UTF_8));

        return voteResource.geteTag().equals(expectedETag);
    }
}
