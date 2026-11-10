package de.pruefprot.upload;

/**
 * Cleans the client-supplied file name. The result is only stored as metadata and used in the
 * download header, never as a file system path.
 */
public final class FilenameSanitizer {

    static final String FALLBACK = "klausur.pdf";
    static final int MAX_LENGTH = 255;

    private FilenameSanitizer() {
    }

    public static String sanitize(String originalFilename) {
        if (originalFilename == null) {
            return FALLBACK;
        }
        // keep only the last path segment (both Unix and Windows separators)
        String name = originalFilename.substring(
                Math.max(originalFilename.lastIndexOf('/'), originalFilename.lastIndexOf('\\')) + 1);
        // drop control characters (CR/LF etc.) and characters that are awkward in headers
        name = name.replaceAll("[\\p{Cntrl}\"]", "");
        name = name.strip();
        // no hidden files or names made of dots only
        name = name.replaceFirst("^\\.+", "");
        if (name.length() > MAX_LENGTH) {
            name = name.substring(0, MAX_LENGTH);
        }
        return name.isBlank() ? FALLBACK : name;
    }
}
