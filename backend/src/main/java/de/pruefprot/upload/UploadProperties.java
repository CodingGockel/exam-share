package de.pruefprot.upload;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.util.unit.DataSize;

/**
 * @param maxSize maximum size of an uploaded PDF (inclusive)
 */
@ConfigurationProperties("pruefprot.upload")
public record UploadProperties(@DefaultValue("10MB") DataSize maxSize) {
}
