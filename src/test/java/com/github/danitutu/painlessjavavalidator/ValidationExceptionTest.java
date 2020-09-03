package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    @DisplayName("WHEN instantiating using a single Violation object THEN return the violation object with its input data")
    void getViolations1() {
        try {
            throw new ValidationException(Violation.of("field.path", "message", "details", singletonMap("other", 1)));
        } catch (ValidationException exception) {
            Violation violation = exception.getViolations().get(0);
            assertEquals("field.path", violation.getField());
            assertEquals("message", violation.getMessage());
            assertEquals("details", violation.getDetails());
            assertEquals(1, violation.getAttributes().size());
            assertEquals(1, violation.getAttributes().get("other"));
        }
    }

    @Test
    @DisplayName(
            "WHEN instantiating using a list of violation objects THEN return the violation objects with their input data")
    void getViolations2() {
        try {
            throw new ValidationException(Arrays.asList(
                    Violation.of("field.path1", "message1", "details1", singletonMap("other1", 1)),
                    Violation.of("field.path2", "message2", "details2", singletonMap("other2", 2))
            ));
        } catch (ValidationException exception) {
            Violation violation1 = exception.getViolations().get(0);
            assertEquals("field.path1", violation1.getField());
            assertEquals("message1", violation1.getMessage());
            assertEquals("details1", violation1.getDetails());
            assertEquals(1, violation1.getAttributes().size());
            assertEquals(1, violation1.getAttributes().get("other1"));

            Violation violation2 = exception.getViolations().get(1);
            assertEquals("field.path2", violation2.getField());
            assertEquals("message2", violation2.getMessage());
            assertEquals("details2", violation2.getDetails());
            assertEquals(1, violation2.getAttributes().size());
            assertEquals(2, violation2.getAttributes().get("other2"));
        }
    }

    @Test
    @DisplayName("WHEN violations present THEN expect exception")
    public void stopIfViolations1() {
        List<Violation> violations = singletonList(Violation.of(null, null, null, null));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> ValidationException.stopIfViolations(violations));
        assertSame(violations, exception.getViolations());
    }

    @Test
    @DisplayName("WHEN violations is empty THEN expect no exception")
    public void stopIfViolations2() {
        ValidationException.stopIfViolations(emptyList());
    }

    @Test
    @DisplayName("WHEN violations is null THEN expect no exception")
    public void stopIfViolations3() {
        ValidationException.stopIfViolations(null);
    }

    @Test
    @DisplayName("WHEN violation present THEN expect exception")
    public void stopIfViolation1() {
        Violation violation = Violation.of(null, null, null, null);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> ValidationException.stopIfViolation(violation));
        assertSame(violation, exception.getViolations().get(0));
        assertEquals(1, exception.getViolations().size());
    }

    @Test
    @DisplayName("WHEN violations is null THEN expect no exception")
    public void stopIfViolation2() {
        ValidationException.stopIfViolation(null);
    }
}