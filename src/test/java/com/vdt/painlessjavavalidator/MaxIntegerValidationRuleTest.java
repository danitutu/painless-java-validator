package com.vdt.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.vdt.painlessjavavalidator.ValidationRule.max;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaxIntegerValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect violation")
    public void max1() {
        Optional<Violation> violation = max("field.path", null, 3);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
    }

    @Test
    @DisplayName("WHEN value is equal to max THEN expect no violation")
    public void max2() {
        Optional<Violation> violation = max("field.path", 4, 4);

        assertTrue(violation.isEmpty());
    }

    @Test
    @DisplayName("WHEN value is greater than max THEN expect violation")
    public void max3() {
        Optional<Violation> violation = max("field.path", 6, 5);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.integer.value.greater.than.max", violation.get().getMessage());
        assertEquals("Value is greater than max.", violation.get().getDetails());
        assertEquals(1, violation.get().getAttributes().size());
        assertEquals(5, violation.get().getAttributes().get("max"));
    }

    @Test
    @DisplayName("WHEN value is smaller than max THEN expect no violation")
    public void max4() {
        Optional<Violation> violation = max("field.path", 3, 5);

        assertTrue(violation.isEmpty());
    }

}