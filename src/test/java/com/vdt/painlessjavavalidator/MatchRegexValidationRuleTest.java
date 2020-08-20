package com.vdt.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.vdt.painlessjavavalidator.ValidationRule.matchRegexRule;
import static org.junit.jupiter.api.Assertions.*;

class MatchRegexValidationRuleTest {

    @Test
    @DisplayName("WHEN value is null THEN expect violation")
    public void matchRegex1() {
        Optional<Violation> violation = matchRegexRule("field.path", null, "[A-Z0-9]+");

        assertTrue(violation.isPresent());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
    }

    @Test
    @DisplayName("WHEN value is not matching the regex exp THEN expect violation")
    public void matchRegex2() {
        Optional<Violation> violation = matchRegexRule("field.path", "TEST1234", "[A-Z]+");

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.string.value.regex.no.match", violation.get().getMessage());
        assertEquals("Value does not match the expected regex.", violation.get().getDetails());
        assertEquals(1, violation.get().getAttributes().size());
        assertEquals("[A-Z]+", violation.get().getAttributes().get("regexPattern"));
    }

    @Test
    @DisplayName("WHEN value is matching the regex exp THEN expect no violation")
    public void matchRegex3() {
        Optional<Violation> violation = matchRegexRule("field.path", "TEST1234", "[A-Z0-9]+");

        assertFalse(violation.isPresent());
    }

}