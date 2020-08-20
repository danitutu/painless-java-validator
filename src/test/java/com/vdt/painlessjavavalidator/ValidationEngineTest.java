package com.vdt.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.vdt.painlessjavavalidator.ValidationEngine.validate;
import static com.vdt.painlessjavavalidator.ValidationRule.notBlank;
import static com.vdt.painlessjavavalidator.ValidationRule.notNull;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class ValidationEngineTest {

    @Test
    @DisplayName("WHEN a single rule is provided and the rule evaluation return a violation THEN return a list containing a single violation")
    void validate1() {
        Violation violation = Violation.of("a", "b", "c");

        List<Violation> violations = validate(() -> Optional.of(violation));

        assertEquals(1, violations.size());
        assertSame(violation, violations.get(0));
    }

    @Test
    @DisplayName("WHEN a single rule is provided and the rule evaluation doesn't return any violations THEN return empty list")
    void validate2() {
        List<Violation> violations = validate(Optional::empty);

        assertEquals(0, violations.size());
    }

    @Test
    @DisplayName("WHEN multiple rules are provided and each rule evaluation returns a violation THEN return containing all violations")
    void validate3() {
        Violation violation1 = Violation.of("a", "b", "c");
        Violation violation2 = Violation.of("c", "d", "e");

        List<Violation> violations = validate(asList(
                () -> Optional.of(violation1),
                () -> Optional.of(violation2)
        ));

        assertEquals(2, violations.size());
        assertSame(violation1, violations.get(0));
        assertSame(violation2, violations.get(1));
    }
}