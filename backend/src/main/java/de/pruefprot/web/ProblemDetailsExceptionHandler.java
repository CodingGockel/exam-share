package de.pruefprot.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * All errors are returned as application/problem+json (RFC 9457). Spring's own exceptions and our
 * {@code ErrorResponseException} subclasses are handled by the base class; validation errors
 * additionally list the affected fields. Rejected requests are logged, unexpected errors with stack trace.
 */
@RestControllerAdvice
public class ProblemDetailsExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ProblemDetailsExceptionHandler.class);

    /** German labels for messages about values that could not be converted (e.g. an unknown degree). */
    private static final Map<String, String> FIELD_LABELS = Map.of(
            "degree", "Abschluss",
            "university", "Uni",
            "faculty", "Fakultät",
            "studyProgram", "Studiengang",
            "subject", "Modul",
            "lecturer", "Dozent:in",
            "semester", "Semester",
            "id", "ID");

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String message = error.isBindingFailure()
                    ? "Ungültiger Wert für " + FIELD_LABELS.getOrDefault(error.getField(), error.getField()) + "."
                    : error.getDefaultMessage();
            fieldErrors.putIfAbsent(error.getField(), message);
        }
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status,
                fieldErrors.isEmpty() ? "Ungültige Eingabe." : String.join(" ", fieldErrors.values()));
        problem.setTitle("Ungültige Eingabe");
        problem.setProperty("errors", fieldErrors);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    /** Unconvertible query or path parameter, e.g. {@code ?degree=DIPLOM}. */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
                                                        HttpStatusCode status, WebRequest request) {
        String name = ex.getPropertyName() == null ? "Parameter" : ex.getPropertyName();
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status,
                "Ungültiger Wert für " + FIELD_LABELS.getOrDefault(name, name) + ".");
        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode statusCode, WebRequest request) {
        if (statusCode.is4xxClientError()) {
            String detail = body instanceof ProblemDetail problem ? problem.getDetail() : ex.getMessage();
            log.info("Request rejected: status={} detail=\"{}\" request={}",
                    statusCode.value(), detail, request.getDescription(false));
        } else {
            log.error("Request failed: status={} request={}", statusCode.value(), request.getDescription(false), ex);
        }
        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    /** Anything unexpected: log with stack trace, answer with a generic 500 problem (no internals). */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUnexpected(Exception ex, WebRequest request) {
        log.error("Unexpected error: request={}", request.getDescription(false), ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500),
                "Interner Serverfehler.");
        return ResponseEntity.status(500).body(problem);
    }
}
