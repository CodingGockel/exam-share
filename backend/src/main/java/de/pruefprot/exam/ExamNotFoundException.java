package de.pruefprot.exam;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

import java.util.UUID;

public class ExamNotFoundException extends ErrorResponseException {

    public ExamNotFoundException(UUID id) {
        super(HttpStatus.NOT_FOUND,
                ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Klausur " + id + " wurde nicht gefunden."),
                null);
    }
}
