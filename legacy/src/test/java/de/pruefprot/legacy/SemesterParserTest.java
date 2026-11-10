package de.pruefprot.legacy;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class SemesterParserTest {

    @Parameters(name = "{index}: \"{0}\" -> {1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"WS24/25", "WS24/25", Semester.Term.WINTER, 2024},
                {"SS25", "SS25", Semester.Term.SUMMER, 2025},
                {"ws19/20", "WS19/20", Semester.Term.WINTER, 2019},
                {"  SS07 ", "SS07", Semester.Term.SUMMER, 2007},
                {"WiSe 2019/20", "WS19/20", Semester.Term.WINTER, 2019},
                {"WiSe 2019/2020", "WS19/20", Semester.Term.WINTER, 2019},
                {"SoSe 2021", "SS21", Semester.Term.SUMMER, 2021},
                {"Wintersemester 2022/2023", "WS22/23", Semester.Term.WINTER, 2022},
                {"Sommersemester 2023", "SS23", Semester.Term.SUMMER, 2023},
                {"WS 2009 / 10", "WS09/10", Semester.Term.WINTER, 2009},
        });
    }

    private final String input;
    private final String expectedCanonical;
    private final Semester.Term expectedTerm;
    private final int expectedStartYear;

    private SemesterParser parser;

    public SemesterParserTest(String input, String expectedCanonical, Semester.Term expectedTerm,
                              int expectedStartYear) {
        this.input = input;
        this.expectedCanonical = expectedCanonical;
        this.expectedTerm = expectedTerm;
        this.expectedStartYear = expectedStartYear;
    }

    @Before
    public void setUp() {
        parser = new SemesterParser();
    }

    @Test
    public void parsesToCanonicalForm() {
        assertThat(parser.parse(input).toCanonical(), is(equalTo(expectedCanonical)));
    }

    @Test
    public void detectsTermAndStartYear() {
        Semester semester = parser.parse(input);
        assertEquals(expectedTerm, semester.getTerm());
        assertEquals(expectedStartYear, semester.getStartYear());
    }
}
