package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NotNullValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect violation")
    public void notNull1() {
        Optional<Violation> violation = ValidationRule.notNullRule("field.path", null);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getField());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
        assertNull(violation.get().getAttributes());
    }

    @Test
    @DisplayName("WHEN value is not null THEN expect no violation")
    public void notNull2() {
        Optional<Violation> violation = ValidationRule.notNullRule("field.path", new Object());

        assertFalse(violation.isPresent());
    }

}