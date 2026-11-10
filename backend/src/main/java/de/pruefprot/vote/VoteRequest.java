package de.pruefprot.vote;

import jakarta.validation.constraints.NotNull;

public record VoteRequest(@NotNull(message = "direction muss UP oder DOWN sein.") VoteDirection direction) {
}
