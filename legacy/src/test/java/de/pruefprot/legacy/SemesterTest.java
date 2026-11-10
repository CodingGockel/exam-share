package de.pruefprot.legacy;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SemesterTest {

    @Test
    public void winterSemesterAcrossTheCenturyBoundary() {
        assertEquals("WS99/00", new Semester(Semester.Term.WINTER, 1999).toCanonical());
    }

    @Test
    public void summerSemesterUsesTwoDigits() {
        assertEquals("SS05", new Semester(Semester.Term.SUMMER, 2005).toCanonical());
    }

    @Test
    public void equalSemestersAreEqual() {
        Semester a = new Semester(Semester.Term.WINTER, 2020);
        Semester b = new Semester(Semester.Term.WINTER, 2020);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void termMatters() {
        assertThat(new Semester(Semester.Term.WINTER, 2020), is(not(new Semester(Semester.Term.SUMMER, 2020))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsMissingTerm() {
        new Semester(null, 2020);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejectsYearOutOfRange() {
        new Semester(Semester.Term.SUMMER, 1850);
    }
}
