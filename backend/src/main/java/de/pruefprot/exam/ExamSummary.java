package de.pruefprot.exam;

import java.time.Instant;
import java.util.UUID;

/**
 * An exam without its file content, used for listings.
 */
public record ExamSummary(
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
        Instant createdAt) {

    static ExamSummary of(Exam exam) {
        return new ExamSummary(exam.getId(), exam.getUniversity(), exam.getFaculty(), exam.getStudyProgram(),
                exam.getDegree(), exam.getSubject(), exam.getLecturer(), exam.getSemester(),
                exam.getOriginalFilename(), exam.getSizeBytes(), exam.getUpvotes(), exam.getDownvotes(),
                exam.getCreatedAt());
    }
}
