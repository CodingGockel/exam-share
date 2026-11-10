package de.pruefprot.legacy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Imports exam metadata from the old semicolon-separated CSV export.
 *
 * <pre>
 * Fach;Dozent;Semester;Datei
 * # comment lines and empty lines are ignored
 * Analysis I;Dr. Müller;WiSe 2019/20;ana1_ws1920.pdf
 * </pre>
 *
 * The header is required (case-insensitive). Values may be wrapped in double quotes.
 */
public class LegacyCsvImporter {

    static final String[] EXPECTED_HEADER = {"fach", "dozent", "semester", "datei"};
    private static final String SEPARATOR = ";";

    private final SemesterParser semesterParser;
    private final Charset charset;

    public LegacyCsvImporter(SemesterParser semesterParser, Charset charset) {
        this.semesterParser = semesterParser;
        this.charset = charset;
    }

    public List<LegacyExamRecord> importFile(File file) throws CsvImportException {
        if (!file.isFile()) {
            throw new CsvImportException("file not found: " + file, null);
        }
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file), charset);
            return importFrom(reader);
        } catch (IOException e) {
            throw new CsvImportException("could not read " + file, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                    // nothing we can do
                }
            }
        }
    }

    public List<LegacyExamRecord> importFrom(Reader source) throws CsvImportException, IOException {
        BufferedReader reader = new BufferedReader(source);
        List<LegacyExamRecord> records = new ArrayList<LegacyExamRecord>();
        boolean headerSeen = false;
        int lineNumber = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (lineNumber == 1 && line.startsWith("﻿")) {
                line = line.substring(1); // UTF-8 BOM written by old Excel versions
            }
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            String[] columns = split(trimmed);
            if (!headerSeen) {
                checkHeader(columns, lineNumber);
                headerSeen = true;
                continue;
            }
            records.add(toRecord(columns, lineNumber));
        }
        if (!headerSeen) {
            throw new CsvImportException(Math.max(lineNumber, 1), "missing header");
        }
        return records;
    }

    private void checkHeader(String[] columns, int lineNumber) throws CsvImportException {
        if (columns.length != EXPECTED_HEADER.length) {
            throw new CsvImportException(lineNumber, "invalid header");
        }
        for (int i = 0; i < columns.length; i++) {
            if (!EXPECTED_HEADER[i].equalsIgnoreCase(columns[i])) {
                throw new CsvImportException(lineNumber, "invalid header column '" + columns[i] + "'");
            }
        }
    }

    private LegacyExamRecord toRecord(String[] columns, int lineNumber) throws CsvImportException {
        if (columns.length != EXPECTED_HEADER.length) {
            throw new CsvImportException(lineNumber,
                    "expected " + EXPECTED_HEADER.length + " columns but found " + columns.length);
        }
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].isEmpty()) {
                throw new CsvImportException(lineNumber, "column '" + EXPECTED_HEADER[i] + "' is empty");
            }
        }
        Semester semester;
        try {
            semester = semesterParser.parse(columns[2]);
        } catch (InvalidSemesterException e) {
            throw new CsvImportException(lineNumber, e.getMessage());
        }
        return new LegacyExamRecord(columns[0], columns[1], semester, columns[3]);
    }

    private static String[] split(String line) {
        String[] parts = line.split(SEPARATOR, -1);
        for (int i = 0; i < parts.length; i++) {
            String value = parts[i].trim();
            if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1).trim();
            }
            parts[i] = value;
        }
        return parts;
    }
}
