package de.pruefprot.upload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.util.unit.DataSize;
import org.springframework.web.ErrorResponseException;

public class UploadTooLargeException extends ErrorResponseException {

    public UploadTooLargeException(long maxBytes) {
        super(HttpStatus.CONTENT_TOO_LARGE, problem(maxBytes), null);
    }

    private static ProblemDetail problem(long maxBytes) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONTENT_TOO_LARGE,
                "Die Datei ist zu groß (maximal " + DataSize.ofBytes(maxBytes).toMegabytes() + " MB).");
        problem.setProperty("maxBytes", maxBytes);
        return problem;
    }
}
