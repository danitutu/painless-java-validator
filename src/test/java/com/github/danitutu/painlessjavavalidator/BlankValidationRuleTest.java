package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BlankValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect no violation")
    public void blank1() {
        Optional<Violation> violation = ValidationRule.blankRule("field.path", null);

        assertFalse(violation.isPresent());
    }

    @Test
    @DisplayName("WHEN value is empty string THEN expect no violation")
    public void blank2() {
        Optional<Violation> violation = ValidationRule.blankRule("field.path", "");

        assertFalse(violation.isPresent());
    }

    @Test
    @DisplayName("WHEN value is filled in THEN expect violation")
    public void blank3() {
        Optional<Violation> violation = ValidationRule.blankRule("field.path", "a value");

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getField());
        assertEquals("validation.error.value.is.not.blank", violation.get().getMessage());
        assertEquals("The value is empty.", violation.get().getDetails());
        assertNull(violation.get().getAttributes());
    }

    @Test
    @DisplayName("WHEN value has only spaces THEN expect no violation")
    public void blank4() {
        Optional<Violation> violation = ValidationRule.blankRule("field.path", "   ");

        assertFalse(violation.isPresent());
    }

}