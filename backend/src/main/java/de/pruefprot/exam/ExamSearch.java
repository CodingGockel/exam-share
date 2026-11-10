package de.pruefprot.exam;

import java.util.List;

/**
 * Custom repository fragment for the filterable exam list (without file content).
 */
public interface ExamSearch {

    List<ExamSummary> search(ExamFilter filter);

    /** Distinct, sorted values of a text column, e.g. all universities (for input suggestions). */
    List<String> distinctValues(SuggestionField field, int limit);

    enum SuggestionField {
        UNIVERSITY("university"),
        FACULTY("faculty"),
        STUDY_PROGRAM("studyProgram");

        private final String attribute;

        SuggestionField(String attribute) {
            this.attribute = attribute;
        }

        String attribute() {
            return attribute;
        }
    }
}
