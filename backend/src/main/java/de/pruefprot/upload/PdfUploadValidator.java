package de.pruefprot.upload;

import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Checks uploaded files: not empty, within the size limit, PDF content type and PDF magic bytes.
 */
@Component
public class PdfUploadValidator {

    private static final byte[] PDF_MAGIC_BYTES = "%PDF-".getBytes(StandardCharsets.US_ASCII);

    private final UploadProperties properties;

    public PdfUploadValidator(UploadProperties properties) {
        this.properties = properties;
    }

    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidUploadException("Die Datei ist leer.");
        }
        long maxBytes = properties.maxSize().toBytes();
        if (file.getSize() > maxBytes) {
            throw new UploadTooLargeException(maxBytes);
        }
        if (!isPdfContentType(file.getContentType())) {
            throw new InvalidUploadException("Es sind nur PDF-Dateien erlaubt.");
        }
        if (!startsWithPdfMagicBytes(file)) {
            throw new InvalidUploadException("Die Datei ist kein gültiges PDF.");
        }
    }

    private static boolean isPdfContentType(String contentType) {
        if (contentType == null) {
            return false;
        }
        try {
            return MediaType.APPLICATION_PDF.equalsTypeAndSubtype(MediaType.parseMediaType(contentType));
        } catch (InvalidMediaTypeException e) {
            return false;
        }
    }

    private static boolean startsWithPdfMagicBytes(MultipartFile file) {
        try (InputStream in = file.getInputStream()) {
            byte[] header = in.readNBytes(PDF_MAGIC_BYTES.length);
            return Arrays.equals(header, PDF_MAGIC_BYTES);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
