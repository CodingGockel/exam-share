package de.pruefprot.exam;

import java.time.Instant;
import java.util.UUID;

public record ExamResponse(
        UUID id,
        String university,
        String faculty,
        String studyProgram,
        Degree degree,
        String subject,
        String lecturer,
        String semester,
        String originalFilename,
        long sizeBytes,
        int upvotes,
        int downvotes,
        double score,
        boolean collapsed,
        Instant createdAt) {
}
