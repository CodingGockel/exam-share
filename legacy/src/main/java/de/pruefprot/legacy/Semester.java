package de.pruefprot.legacy;

/**
 * A semester from the old exam archive. Years are stored with four digits.
 */
public final class Semester {

    public enum Term {
        WINTER, SUMMER
    }

    private final Term term;
    private final int startYear;

    public Semester(Term term, int startYear) {
        if (term == null) {
            throw new IllegalArgumentException("term must not be null");
        }
        if (startYear < 1900 || startYear > 2099) {
            throw new IllegalArgumentException("start year out of range: " + startYear);
        }
        this.term = term;
        this.startYear = startYear;
    }

    public Term getTerm() {
        return term;
    }

    public int getStartYear() {
        return startYear;
    }

    /**
     * Canonical short form as used by the new platform, e.g. {@code WS24/25} or {@code SS25}.
     */
    public String toCanonical() {
        int shortYear = startYear % 100;
        if (term == Term.SUMMER) {
            return String.format("SS%02d", shortYear);
        }
        return String.format("WS%02d/%02d", shortYear, (shortYear + 1) % 100);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Semester)) {
            return false;
        }
        Semester other = (Semester) o;
        return term == other.term && startYear == other.startYear;
    }

    @Override
    public int hashCode() {
        return 31 * term.hashCode() + startYear;
    }

    @Override
    public String toString() {
        return toCanonical();
    }
}
