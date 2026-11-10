package de.pruefprot.vote;

import java.util.UUID;

/**
 * Vote counters of the exam after the vote was counted.
 */
public record VoteResponse(UUID examId, int upvotes, int downvotes, double score, boolean collapsed) {
}
