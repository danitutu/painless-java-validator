package com.vdt.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    @DisplayName("WHEN instantiating using a single Violation object THEN return the violation object with its input data")
    void getViolations1() {
        try {
            throw new ValidationException(Violation.of("field.path", "message", "details", singletonMap("other", 1)));
        } catch (ValidationException exception) {
            Violation violation = exception.getViolations().get(0);
            assertEquals("field.path", violation.getFieldPath());
            assertEquals("message", violation.getMessage());
            assertEquals("details", violation.getDetails());
            assertEquals(1, violation.getAttributes().size());
            assertEquals(1, violation.getAttributes().get("other"));
        }
    }

    @Test
    @DisplayName("WHEN instantiating using a single a list of violation objects THEN return the violation objects with their input data")
    void getViolations2() {
        try {
            throw new ValidationException(Arrays.asList(
                    Violation.of("field.path1", "message1", "details1", singletonMap("other1", 1)),
                    Violation.of("field.path2", "message2", "details2", singletonMap("other2", 2))
            ));
        } catch (ValidationException exception) {
            Violation violation1 = exception.getViolations().get(0);
            assertEquals("field.path1", violation1.getFieldPath());
            assertEquals("message1", violation1.getMessage());
            assertEquals("details1", violation1.getDetails());
            assertEquals(1, violation1.getAttributes().size());
            assertEquals(1, violation1.getAttributes().get("other1"));

            Violation violation2 = exception.getViolations().get(1);
            assertEquals("field.path2", violation2.getFieldPath());
            assertEquals("message2", violation2.getMessage());
            assertEquals("details2", violation2.getDetails());
            assertEquals(1, violation2.getAttributes().size());
            assertEquals(2, violation2.getAttributes().get("other2"));
        }
    }
}