package de.pruefprot.exam;

/**
 * Optional search criteria. {@code null} or blank values are ignored.
 *
 * @param university   partial match, case-insensitive
 * @param faculty      partial match, case-insensitive
 * @param studyProgram partial match, case-insensitive
 * @param degree       exact match
 * @param subject      partial match, case-insensitive
 * @param lecturer     partial match, case-insensitive
 * @param semester     exact match
 */
public record ExamFilter(
        String university,
        String faculty,
        String studyProgram,
        Degree degree,
        String subject,
        String lecturer,
        String semester) {

    public static ExamFilter none() {
        return new ExamFilter(null, null, null, null, null, null, null);
    }
}
