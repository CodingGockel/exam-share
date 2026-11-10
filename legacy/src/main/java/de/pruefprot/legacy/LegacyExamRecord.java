package de.pruefprot.legacy;

/**
 * One row of the old CSV export.
 */
public final class LegacyExamRecord {

    private final String subject;
    private final String lecturer;
    private final Semester semester;
    private final String fileName;

    public LegacyExamRecord(String subject, String lecturer, Semester semester, String fileName) {
        this.subject = subject;
        this.lecturer = lecturer;
        this.semester = semester;
        this.fileName = fileName;
    }

    public String getSubject() {
        return subject;
    }

    public String getLecturer() {
        return lecturer;
    }

    public Semester getSemester() {
        return semester;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return subject + " (" + lecturer + ", " + semester + ")";
    }
}
