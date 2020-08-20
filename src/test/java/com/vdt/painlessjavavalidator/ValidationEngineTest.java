package com.vdt.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.vdt.painlessjavavalidator.ValidationEngine.validate;
import static com.vdt.painlessjavavalidator.ValidationRule.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationEngineTest {

    @Test
    @DisplayName(
            "WHEN multiple rules are provided and each rule evaluation returns a violation except the last one THEN return containing all violations")
    void validate1() {
        List<Violation> violations = validate(
                notNull("nullField", null),
                notBlank("blankField", ""),
                lengthBetween("validLengthBetweenField", "a string", 2, 10)
        );

        assertEquals(2, violations.size());
        assertEquals("nullField", violations.get(0).getFieldPath());
        assertEquals("blankField", violations.get(1).getFieldPath());
    }
}