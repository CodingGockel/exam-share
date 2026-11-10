package de.pruefprot.legacy;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.containsString;

public class SemesterParserErrorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private SemesterParser parser;

    @Before
    public void setUp() {
        parser = new SemesterParser();
    }

    @Test(expected = InvalidSemesterException.class)
    public void rejectsNull() {
        parser.parse(null);
    }

    @Test(expected = InvalidSemesterException.class)
    public void rejectsBlankInput() {
        parser.parse("   ");
    }

    @Test(expected = InvalidSemesterException.class)
    public void rejectsUnknownPrefix() {
        parser.parse("HS24");
    }

    @Test(expected = InvalidSemesterException.class)
    public void rejectsSummerSemesterWithTwoYears() {
        parser.parse("SS24/25");
    }

    @Test
    public void rejectsNonConsecutiveWinterYears() {
        thrown.expect(InvalidSemesterException.class);
        thrown.expectMessage(containsString("consecutive"));

        parser.parse("WS24/26");
    }

    @Test
    public void reportsTheOriginalInputForUnknownFormats() {
        thrown.expect(InvalidSemesterException.class);
        thrown.expectMessage("unknown semester format: Winter 24");

        parser.parse("Winter 24");
    }
}
