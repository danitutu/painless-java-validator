package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NotEmptyValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect violation")
    public void notEmpty1() {
        Optional<Violation> violation = ValidationRule.notEmptyRule("field.path", null);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getField());
        assertEquals("validation.error.value.is.not.empty", violation.get().getMessage());
        assertEquals("The value is not empty.", violation.get().getDetails());
        assertNull(violation.get().getAttributes());
    }

    @Test
    @DisplayName("WHEN value is empty string THEN expect no violation")
    public void notEmpty2() {
        Optional<Violation> violation = ValidationRule.notEmptyRule("field.path", "");

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getField());
        assertEquals("validation.error.value.is.not.empty", violation.get().getMessage());
        assertEquals("The value is not empty.", violation.get().getDetails());
        assertNull(violation.get().getAttributes());
    }

    @Test
    @DisplayName("WHEN value is filled in THEN expect violation")
    public void notEmpty3() {
        Optional<Violation> violation = ValidationRule.notEmptyRule("field.path", "a value");

        assertFalse(violation.isPresent());
    }

}