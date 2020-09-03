package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class EqualsToStringValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect violation")
    public void equalsTo1() {
        Optional<Violation> violation = ValidationRule.equalsToRule("field.path", null, null);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getField());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
    }

    @Test
    @DisplayName("WHEN values are not equal THEN expect violation")
    public void equalsTo2() {
        Optional<Violation> violation = ValidationRule.equalsToRule("field.path", "s1", "s2");

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getField());
        assertEquals("validation.error.string.is.not.equal", violation.get().getMessage());
        assertEquals("The value is not equal to the other value.", violation.get().getDetails());
        assertEquals(1, violation.get().getAttributes().size());
        assertEquals("s2", violation.get().getAttributes().get("other"));
    }

    @Test
    @DisplayName("WHEN values are equal THEN expect no violation")
    public void equalsTo3() {
        Optional<Violation> violation = ValidationRule.equalsToRule("field.path", "s1", "s1");

        assertFalse(violation.isPresent());
    }

}