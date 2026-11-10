package de.pruefprot.legacy;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LegacyCsvImporterTest {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Locale originalLocale;
    private LegacyCsvImporter importer;

    @Before
    public void setUp() {
        // Turkish locale is the classic trap for toUpperCase(): parsing must not depend on the default locale
        originalLocale = Locale.getDefault();
        Locale.setDefault(new Locale("tr", "TR"));
        importer = new LegacyCsvImporter(new SemesterParser(), UTF8);
    }

    @After
    public void tearDown() {
        Locale.setDefault(originalLocale);
    }

    @Test
    public void importsAllRecords() throws Exception {
        File csv = writeCsv(
                "Fach;Dozent;Semester;Datei",
                "Analysis I;Dr. Müller;WiSe 2019/20;ana1_ws1920.pdf",
                "Lineare Algebra;Prof. Schmidt;SS21;linalg_ss21.pdf");

        List<LegacyExamRecord> records = importer.importFile(csv);

        assertThat(records, hasSize(2));
        assertEquals("Analysis I", records.get(0).getSubject());
        assertEquals("Dr. Müller", records.get(0).getLecturer());
        assertThat(records.get(0).getSemester().toCanonical(), is(equalTo("WS19/20")));
        assertThat(records.get(1).getFileName(), is("linalg_ss21.pdf"));
    }

    @Test
    public void ignoresCommentsBlankLinesAndQuotes() throws Exception {
        File csv = writeCsv(
                "# Export vom 03.04.2011",
                "FACH;DOZENT;SEMESTER;DATEI",
                "",
                "\"Datenbanken\";\"Dr. Weber\";\"Sommersemester 2010\";\"db.pdf\"",
                "   # noch ein Kommentar");

        List<LegacyExamRecord> records = importer.importFile(csv);

        assertThat(records, hasSize(1));
        assertThat(records.get(0).getSubject(), is("Datenbanken"));
        assertThat(records.get(0).getSemester().toCanonical(), is("SS10"));
    }

    @Test
    public void stripsUtf8ByteOrderMark() throws Exception {
        File csv = writeCsv("﻿Fach;Dozent;Semester;Datei", "Physik;Dr. Kurz;WS10/11;physik.pdf");

        assertThat(importer.importFile(csv), hasSize(1));
    }

    @Test
    public void headerOnlyGivesEmptyResult() throws Exception {
        File csv = writeCsv("Fach;Dozent;Semester;Datei");

        assertThat(importer.importFile(csv), is(empty()));
    }

    @Test
    public void reportsLineNumberOfInvalidSemester() throws Exception {
        File csv = writeCsv(
                "Fach;Dozent;Semester;Datei",
                "Analysis I;Dr. Müller;WS19/20;a.pdf",
                "Analysis II;Dr. Müller;WS19/21;b.pdf");

        thrown.expect(CsvImportException.class);
        thrown.expectMessage(containsString("line 3"));
        thrown.expectMessage(containsString("consecutive"));

        importer.importFile(csv);
    }

    @Test
    public void rejectsWrongColumnCount() throws Exception {
        File csv = writeCsv(
                "Fach;Dozent;Semester;Datei",
                "Analysis I;Dr. Müller;WS19/20");

        thrown.expect(CsvImportException.class);
        thrown.expectMessage("line 2: expected 4 columns but found 3");

        importer.importFile(csv);
    }

    @Test
    public void rejectsEmptyColumn() throws Exception {
        File csv = writeCsv(
                "Fach;Dozent;Semester;Datei",
                "Analysis I;;WS19/20;a.pdf");

        thrown.expect(CsvImportException.class);
        thrown.expectMessage(containsString("'dozent' is empty"));

        importer.importFile(csv);
    }

    @Test(expected = CsvImportException.class)
    public void rejectsMissingHeader() throws Exception {
        importer.importFile(writeCsv("Analysis I;Dr. Müller;WS19/20;a.pdf"));
    }

    @Test(expected = CsvImportException.class)
    public void rejectsEmptyFile() throws Exception {
        importer.importFile(tempFolder.newFile("empty.csv"));
    }

    @Test(expected = CsvImportException.class)
    public void rejectsMissingFile() throws Exception {
        importer.importFile(new File(tempFolder.getRoot(), "does-not-exist.csv"));
    }

    private File writeCsv(String... lines) throws IOException {
        File file = tempFolder.newFile();
        Writer writer = new OutputStreamWriter(new FileOutputStream(file), UTF8);
        try {
            for (String line : lines) {
                writer.write(line);
                writer.write("\n");
            }
        } finally {
            writer.close();
        }
        return file;
    }
}
