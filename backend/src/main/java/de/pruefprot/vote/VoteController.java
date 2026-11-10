package de.pruefprot.vote;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/exams/{id}/votes")
    @ResponseStatus(HttpStatus.CREATED)
    public VoteResponse vote(@PathVariable UUID id,
                             @Valid @RequestBody VoteRequest request,
                             @RequestAttribute(VoterCookieFilter.VOTER_ID_ATTRIBUTE) UUID voterId) {
        return voteService.vote(id, voterId, request.direction());
    }
}
