package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LengthBetweenValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect violation")
    public void lengthBetween1() {
        Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", null, 3, 10);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
    }

    @Test
    @DisplayName("WHEN value is empty string and the limit is equal to inferior limit THEN expect no violations")
    public void lengthBetween2() {
        Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "", 0, 10);

        assertFalse(violation.isPresent());
    }

    @Test
    @DisplayName("WHEN value is empty string and the value length is outside interval THEN expect violation")
    public void lengthBetween12() {
        Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "", 1, 10);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.string.value.not.between", violation.get().getMessage());
        assertEquals("Value is not in range.", violation.get().getDetails());
        assertEquals(2, violation.get().getAttributes().size());
        assertEquals(1, violation.get().getAttributes().get("min"));
        assertEquals(10, violation.get().getAttributes().get("max"));
    }

    @Test
    @DisplayName("WHEN value length is equal to min THEN expect no violation")
    public void lengthBetween4() {
        Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 4, 10);

        assertFalse(violation.isPresent());
    }

    @Test
    @DisplayName("WHEN value length is equal to max THEN expect no violation")
    public void lengthBetween5() {
        Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 2, 4);

        assertFalse(violation.isPresent());
    }

    @Test
    @DisplayName("WHEN value length is between min and max THEN expect no violation")
    public void lengthBetween6() {
        Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 2, 5);

        assertFalse(violation.isPresent());
    }

    @Test
    @DisplayName("WHEN value length is smaller than min THEN expect violation")
    public void lengthBetween7() {
        Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 5, 7);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.string.value.not.between", violation.get().getMessage());
        assertEquals("Value is not in range.", violation.get().getDetails());
        assertEquals(2, violation.get().getAttributes().size());
        assertEquals(5, violation.get().getAttributes().get("min"));
        assertEquals(7, violation.get().getAttributes().get("max"));
    }

    @Test
    @DisplayName("WHEN value length is greater than max THEN expect violation")
    public void lengthBetween8() {
        Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 1, 3);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.string.value.not.between", violation.get().getMessage());
        assertEquals("Value is not in range.", violation.get().getDetails());
        assertEquals(2, violation.get().getAttributes().size());
        assertEquals(1, violation.get().getAttributes().get("min"));
        assertEquals(3, violation.get().getAttributes().get("max"));
    }

    @Test
    @DisplayName("WHEN min greater than max THEN exception")
    public void lengthBetween9() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> ValidationRule.lengthBetweenRule("field.path", "test", 3, 2));

        assertEquals("min is greater than max", ex.getMessage());
    }

    @Test
    @DisplayName("WHEN value length different than min and min=max THEN expect violation")
    public void lengthBetween10() {
        Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 3, 3);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.string.value.not.between", violation.get().getMessage());
        assertEquals("Value is not in range.", violation.get().getDetails());
        assertEquals(2, violation.get().getAttributes().size());
        assertEquals(3, violation.get().getAttributes().get("min"));
        assertEquals(3, violation.get().getAttributes().get("max"));
    }

    @Test
    @DisplayName("WHEN value length=min=max THEN expect no violation")
    public void lengthBetween11() {
        Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 4, 4);

        assertFalse(violation.isPresent());
    }

}