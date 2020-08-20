package com.vdt.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.vdt.painlessjavavalidator.ValidationRule.inRange;
import static org.junit.jupiter.api.Assertions.*;

class InRangeIntegerValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect violation")
    public void inRange1() {
        Optional<Violation> violation = inRange("field.path", null, 3, 10);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
    }

    @Test
    @DisplayName("WHEN value is equal to min THEN expect no violation")
    public void inRange2() {
        Optional<Violation> violation = inRange("field.path", 4, 4, 10);

        assertTrue(violation.isEmpty());
    }

    @Test
    @DisplayName("WHEN value is equal to max THEN expect no violation")
    public void inRange3() {
        Optional<Violation> violation = inRange("field.path", 4, 2, 4);

        assertTrue(violation.isEmpty());
    }

    @Test
    @DisplayName("WHEN value is between min and max THEN expect no violation")
    public void inRange4() {
        Optional<Violation> violation = inRange("field.path", 4, 2, 5);

        assertTrue(violation.isEmpty());
    }

    @Test
    @DisplayName("WHEN value is smaller than min THEN expect violation")
    public void inRange5() {
        Optional<Violation> violation = inRange("field.path", 3, 5, 7);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.integer.value.not.in.range", violation.get().getMessage());
        assertEquals("Value is not in range.", violation.get().getDetails());
        assertEquals(2, violation.get().getAttributes().size());
        assertEquals(5, violation.get().getAttributes().get("min"));
        assertEquals(7, violation.get().getAttributes().get("max"));
    }

    @Test
    @DisplayName("WHEN value is greater than max THEN expect violation")
    public void inRange6() {
        Optional<Violation> violation = inRange("field.path", 5, 1, 3);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.integer.value.not.in.range", violation.get().getMessage());
        assertEquals("Value is not in range.", violation.get().getDetails());
        assertEquals(2, violation.get().getAttributes().size());
        assertEquals(1, violation.get().getAttributes().get("min"));
        assertEquals(3, violation.get().getAttributes().get("max"));
    }

    @Test
    @DisplayName("WHEN min greater than max THEN exception")
    public void inRange7() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> inRange("field.path", 0, 3, 2));

        assertEquals("min is greater than max", ex.getMessage());
    }

    @Test
    @DisplayName("WHEN value different than min and min=max THEN expect violation")
    public void inRange8() {
        Optional<Violation> violation = inRange("field.path", 2, 3, 3);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.integer.value.not.in.range", violation.get().getMessage());
        assertEquals("Value is not in range.", violation.get().getDetails());
        assertEquals(2, violation.get().getAttributes().size());
        assertEquals(3, violation.get().getAttributes().get("min"));
        assertEquals(3, violation.get().getAttributes().get("max"));
    }

    @Test
    @DisplayName("WHEN value=min=max THEN expect violation")
    public void inRange9() {
        Optional<Violation> violation = inRange("field.path", 4, 4, 4);

        assertTrue(violation.isEmpty());
    }

}