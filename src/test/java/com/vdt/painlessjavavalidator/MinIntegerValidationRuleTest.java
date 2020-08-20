package com.vdt.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.vdt.painlessjavavalidator.ValidationRule.min;
import static org.junit.jupiter.api.Assertions.*;

class MinIntegerValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect violation")
    public void min1() {
        Optional<Violation> violation = min("field.path", null, 3);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
    }

    @Test
    @DisplayName("WHEN value is equal to min THEN expect no violation")
    public void min2() {
        Optional<Violation> violation = min("field.path", 4, 4);

        assertFalse(violation.isPresent());
    }

    @Test
    @DisplayName("WHEN value is smaller than min THEN expect violation")
    public void min3() {
        Optional<Violation> violation = min("field.path", 2, 5);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.integer.value.smaller.than.min", violation.get().getMessage());
        assertEquals("Value is smaller than min.", violation.get().getDetails());
        assertEquals(1, violation.get().getAttributes().size());
        assertEquals(5, violation.get().getAttributes().get("min"));
    }

    @Test
    @DisplayName("WHEN value is greater than min THEN expect no violation")
    public void min4() {
        Optional<Violation> violation = min("field.path", 3, 1);

        assertFalse(violation.isPresent());
    }

}