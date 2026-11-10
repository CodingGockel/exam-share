package de.pruefprot.semester;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SemesterValidator implements ConstraintValidator<ValidSemester, String> {

    private static final Pattern WINTER = Pattern.compile("WS(\\d{2})/(\\d{2})");
    private static final Pattern SUMMER = Pattern.compile("SS\\d{2}");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || isValidSemester(value);
    }

    /**
     * {@code SS25} or {@code WS24/25}. For winter semesters the second year must follow the first one
     * (two-digit years, so {@code WS99/00} is valid as well).
     */
    public static boolean isValidSemester(String value) {
        if (value == null) {
            return false;
        }
        if (SUMMER.matcher(value).matches()) {
            return true;
        }
        Matcher winter = WINTER.matcher(value);
        if (!winter.matches()) {
            return false;
        }
        int firstYear = Integer.parseInt(winter.group(1));
        int secondYear = Integer.parseInt(winter.group(2));
        return (firstYear + 1) % 100 == secondYear;
    }
}
