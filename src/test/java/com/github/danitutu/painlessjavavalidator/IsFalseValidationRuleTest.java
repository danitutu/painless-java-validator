package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IsFalseValidationRuleTest {

    @Test
    @DisplayName("WHEN condition is null THEN expect exception")
    void isFalseRule1() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationRule.isFalseRule(null, null));

        assertEquals("Condition cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("WHEN violation is null THEN expect exception")
    void isFalseRule2() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> ValidationRule.isFalseRule(() -> true, null));

        assertEquals("Violation provider cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("WHEN condition is evaluated to false THEN expect no violation")
    void isFalseRule3() {
        Violation violation = Violation.of(null, null, null, null);

        Optional<Violation> result = ValidationRule.isFalseRule(() -> false, violation);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("WHEN condition is evaluated to true THEN expect violation")
    void isFalseRule4() {
        Violation violation = Violation.of(null, null, null, null);

        Optional<Violation> result = ValidationRule.isFalseRule(() -> true, violation);

        assertTrue(result.isPresent());
        assertSame(violation, result.get());
    }

}