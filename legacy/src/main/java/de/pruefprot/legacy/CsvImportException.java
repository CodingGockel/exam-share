package de.pruefprot.legacy;

public class CsvImportException extends Exception {

    private final int lineNumber;

    public CsvImportException(int lineNumber, String message) {
        super("line " + lineNumber + ": " + message);
        this.lineNumber = lineNumber;
    }

    public CsvImportException(String message, Throwable cause) {
        super(message, cause);
        this.lineNumber = -1;
    }

    /** 1-based line number, or -1 if the error is not related to a line. */
    public int getLineNumber() {
        return lineNumber;
    }
}
