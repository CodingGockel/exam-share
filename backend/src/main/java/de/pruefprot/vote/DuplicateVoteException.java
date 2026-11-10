package de.pruefprot.vote;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class DuplicateVoteException extends ErrorResponseException {

    public DuplicateVoteException() {
        super(HttpStatus.CONFLICT,
                ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, "Du hast für diese Klausur bereits abgestimmt."),
                null);
    }
}
