package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationPositiveOrZero;
import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateAll;
import static com.github.danitutu.painlessjavavalidator.ValidationRule.negative;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NegativeValidationRuleTest {

    @Test
    @DisplayName("WHEN is positive THEN expect no violation")
    void negative1() {
        List<Violation> violations = validateAll(
                negative("f.BigDecimal", BigDecimal.valueOf(0.000000000001)),
                negative("f.BigInteger", BigInteger.valueOf(1)),
                negative("f.Double", 0.000000000001D),
                negative("f.Float", 0.000000000001F),
                negative("f.Long", 1L),
                negative("f.Integer", 1)
        );
        violations.forEach(violation -> assertViolationPositiveOrZero(violation, violation.getField()));
    }

    @Test
    @DisplayName("WHEN is zero THEN expect errors")
    void negative2() {
        List<Violation> violations = validateAll(
                negative("f.BigDecimal", BigDecimal.ZERO),
                negative("f.BigInteger", BigInteger.ZERO),
                negative("f.Double", 0.00000000000D),
                negative("f.Float", 0.000000000000F),
                negative("f.Long", 0L),
                negative("f.Integer", 0)
        );
        violations.forEach(violation -> assertViolationPositiveOrZero(violation, violation.getField()));
    }

    @Test
    @DisplayName("WHEN is negative THEN expect errors")
    void negative3() {
        List<Violation> violations = validateAll(
                negative("f.BigDecimal", BigDecimal.valueOf(-0.000000000001)),
                negative("f.BigInteger", BigInteger.valueOf(-1)),
                negative("f.Double", -0.00000000001D),
                negative("f.Float", -0.000000000001F),
                negative("f.Long", -1L),
                negative("f.Integer", -1)
        );
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("WHEN is null THEN expect errors")
    void negative4() {
        List<Violation> violations = validateAll(
                negative("f.BigDecimal", (BigDecimal) null),
                negative("f.BigInteger", (BigInteger) null),
                negative("f.Double", (Double) null),
                negative("f.Float", (Float) null),
                negative("f.Long", (Long) null),
                negative("f.Integer", (Integer) null)
        );
        violations.forEach(violation -> assertViolationIsRequired(violation, violation.getField()));
    }

}
