package de.pruefprot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PruefprotApplication {

    public static void main(String[] args) {
        SpringApplication.run(PruefprotApplication.class, args);
    }
}
