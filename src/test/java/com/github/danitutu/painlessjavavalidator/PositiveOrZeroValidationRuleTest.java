package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationNegative;
import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateAll;
import static com.github.danitutu.painlessjavavalidator.ValidationRule.positive;
import static com.github.danitutu.painlessjavavalidator.ValidationRule.positiveOrZero;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PositiveOrZeroValidationRuleTest {

    @Test
    @DisplayName("WHEN is positive THEN expect no violation")
    void positiveOrZero1() {
        List<Violation> violations = validateAll(
                positiveOrZero("f.BigDecimal", BigDecimal.valueOf(0.000000000001)),
                positiveOrZero("f.BigInteger", BigInteger.valueOf(1)),
                positiveOrZero("f.Double", 0.000000000001D),
                positiveOrZero("f.Float", 0.000000000001F),
                positiveOrZero("f.Long", 1L),
                positiveOrZero("f.Integer", 1)
        );
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("WHEN is zero THEN expect errors")
    void positiveOrZero2() {
        List<Violation> violations = validateAll(
                positiveOrZero("f.BigDecimal", BigDecimal.ZERO),
                positiveOrZero("f.BigInteger", BigInteger.ZERO),
                positiveOrZero("f.Double", 0.00000000000D),
                positiveOrZero("f.Float", 0.000000000000F),
                positiveOrZero("f.Long", 0L),
                positiveOrZero("f.Integer", 0)
        );
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("WHEN is negative THEN expect errors")
    void positiveOrZero3() {
        List<Violation> violations = validateAll(
                positiveOrZero("f.BigDecimal", BigDecimal.valueOf(-0.000000000001)),
                positiveOrZero("f.BigInteger", BigInteger.valueOf(-1)),
                positiveOrZero("f.Double", -0.00000000001D),
                positiveOrZero("f.Float", -0.000000000001F),
                positiveOrZero("f.Long", -1L),
                positiveOrZero("f.Integer", -1)
        );
        violations.forEach(violation -> assertViolationNegative(violation, violation.getField()));
    }

    @Test
    @DisplayName("WHEN is null THEN expect errors")
    void positiveOrZero4() {
        List<Violation> violations = validateAll(
                positive("f.BigDecimal", (BigDecimal) null),
                positive("f.BigInteger", (BigInteger) null),
                positiveOrZero("f.Double", (Double) null),
                positiveOrZero("f.Float", (Float) null),
                positiveOrZero("f.Long", (Long) null),
                positiveOrZero("f.Integer", (Integer) null)
        );
        violations.forEach(violation -> assertViolationIsRequired(violation, violation.getField()));
    }

}
