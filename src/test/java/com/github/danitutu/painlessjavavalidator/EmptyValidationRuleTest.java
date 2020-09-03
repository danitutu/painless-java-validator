package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EmptyValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect no violation")
    public void empty1() {
        Optional<Violation> violation = ValidationRule.emptyRule("field.path", null);

        assertFalse(violation.isPresent());
    }

    @Test
    @DisplayName("WHEN value is empty string THEN expect no violation")
    public void empty2() {
        Optional<Violation> violation = ValidationRule.emptyRule("field.path", "");

        assertFalse(violation.isPresent());
    }

    @Test
    @DisplayName("WHEN value is filled in THEN expect violation")
    public void empty3() {
        Optional<Violation> violation = ValidationRule.emptyRule("field.path", "a value");

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getField());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
        assertNull(violation.get().getAttributes());
    }

}