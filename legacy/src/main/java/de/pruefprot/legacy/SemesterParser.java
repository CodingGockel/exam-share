package de.pruefprot.legacy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses the different semester notations found in the old archive:
 * <ul>
 *     <li>{@code WS24/25}, {@code SS25} (short form)</li>
 *     <li>{@code WiSe 2024/25}, {@code SoSe 2025}</li>
 *     <li>{@code Wintersemester 2024/2025}, {@code Sommersemester 2025}</li>
 * </ul>
 * Two-digit years are interpreted as 20xx. Matching is case-insensitive.
 */
public class SemesterParser {

    private static final Pattern WINTER = Pattern.compile(
            "(?:WS|WISE|WINTERSEMESTER)\\s*(\\d{2}|\\d{4})\\s*/\\s*(\\d{2}|\\d{4})");
    private static final Pattern SUMMER = Pattern.compile(
            "(?:SS|SOSE|SOMMERSEMESTER)\\s*(\\d{2}|\\d{4})");

    public Semester parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidSemesterException("semester is empty");
        }
        String normalized = input.trim().toUpperCase(java.util.Locale.ROOT);

        Matcher summer = SUMMER.matcher(normalized);
        if (summer.matches()) {
            return new Semester(Semester.Term.SUMMER, toFullYear(summer.group(1)));
        }

        Matcher winter = WINTER.matcher(normalized);
        if (winter.matches()) {
            int start = toFullYear(winter.group(1));
            int end = toFullYear(winter.group(2));
            if (end != start + 1) {
                throw new InvalidSemesterException("winter semester years must be consecutive: " + input);
            }
            return new Semester(Semester.Term.WINTER, start);
        }

        throw new InvalidSemesterException("unknown semester format: " + input);
    }

    private static int toFullYear(String year) {
        int value = Integer.parseInt(year);
        return year.length() == 2 ? 2000 + value : value;
    }
}
