package de.pruefprot.exam;

import de.pruefprot.semester.ValidSemester;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Text fields of the multipart upload. Strings are trimmed before validation and blank values
 * become {@code null} (see {@code de.pruefprot.web.TrimmingBinderAdvice}).
 */
public record UploadExamRequest(
        @NotNull(message = "Uni ist ein Pflichtfeld.")
        @Size(min = 1, max = 150, message = "Uni muss 1 bis 150 Zeichen lang sein.")
        String university,

        @NotNull(message = "Fakultät ist ein Pflichtfeld.")
        @Size(min = 1, max = 100, message = "Fakultät muss 1 bis 100 Zeichen lang sein.")
        String faculty,

        @NotNull(message = "Studiengang ist ein Pflichtfeld.")
        @Size(min = 1, max = 100, message = "Studiengang muss 1 bis 100 Zeichen lang sein.")
        String studyProgram,

        @NotNull(message = "Abschluss ist ein Pflichtfeld.")
        Degree degree,

        @NotNull(message = "Modul ist ein Pflichtfeld.")
        @Size(min = 1, max = 100, message = "Modul muss 1 bis 100 Zeichen lang sein.")
        String subject,

        @NotNull(message = "Dozent:in ist ein Pflichtfeld.")
        @Size(min = 1, max = 100, message = "Dozent:in muss 1 bis 100 Zeichen lang sein.")
        String lecturer,

        @NotNull(message = "Semester ist ein Pflichtfeld.")
        @ValidSemester
        String semester) {
}
