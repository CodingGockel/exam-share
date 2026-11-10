package de.pruefprot.semester;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Semester in the format {@code WS24/25} or {@code SS25}. A {@code null} value is considered valid,
 * combine with {@code @NotNull} if the field is required.
 */
@Documented
@Constraint(validatedBy = SemesterValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSemester {

    String message() default "Semester muss das Format WS24/25 oder SS25 haben (bei WS aufeinanderfolgende Jahre).";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
