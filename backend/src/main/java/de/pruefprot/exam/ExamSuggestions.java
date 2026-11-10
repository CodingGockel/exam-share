package de.pruefprot.exam;

import java.util.List;

/**
 * Values that already exist, offered as input suggestions in the frontend.
 */
public record ExamSuggestions(List<String> universities, List<String> faculties, List<String> studyPrograms) {
}
