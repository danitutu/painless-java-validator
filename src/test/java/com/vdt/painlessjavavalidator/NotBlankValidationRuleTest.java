package com.vdt.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.vdt.painlessjavavalidator.ValidationRule.notBlankRule;
import static org.junit.jupiter.api.Assertions.*;

class NotBlankValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect violation")
    public void notBlank1() {
        Optional<Violation> violation = notBlankRule("field.path", null);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
        assertNull(violation.get().getAttributes());
    }

    @Test
    @DisplayName("WHEN value is empty string THEN expect violation")
    public void notBlank2() {
        Optional<Violation> violation = notBlankRule("field.path", "");

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
        assertNull(violation.get().getAttributes());
    }

    @Test
    @DisplayName("WHEN value is filled in THEN expect no violation")
    public void notBlank3() {
        Optional<Violation> violation = notBlankRule("field.path", "a value");

        assertFalse(violation.isPresent());
    }

}