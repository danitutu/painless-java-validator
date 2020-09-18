package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationPositive;
import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateAll;
import static com.github.danitutu.painlessjavavalidator.ValidationRule.negativeOrZero;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NegativeOrZeroValidationRuleTest {

    @Test
    @DisplayName("WHEN is positive THEN expect no violation")
    void negativeOrZero1() {
        List<Violation> violations = validateAll(
                negativeOrZero("f.BigDecimal", BigDecimal.valueOf(0.000000000001)),
                negativeOrZero("f.BigInteger", BigInteger.valueOf(1)),
                negativeOrZero("f.Double", 0.000000000001D),
                negativeOrZero("f.Float", 0.000000000001F),
                negativeOrZero("f.Long", 1L),
                negativeOrZero("f.Integer", 1)
        );
        violations.forEach(violation -> assertViolationPositive(violation, violation.getField()));
    }

    @Test
    @DisplayName("WHEN is zero THEN expect errors")
    void negativeOrZero2() {
        List<Violation> violations = validateAll(
                negativeOrZero("f.BigDecimal", BigDecimal.ZERO),
                negativeOrZero("f.BigInteger", BigInteger.ZERO),
                negativeOrZero("f.Double", 0.00000000000D),
                negativeOrZero("f.Float", 0.000000000000F),
                negativeOrZero("f.Long", 0L),
                negativeOrZero("f.Integer", 0)
        );
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("WHEN is negative THEN expect errors")
    void negativeOrZero3() {
        List<Violation> violations = validateAll(
                negativeOrZero("f.BigDecimal", BigDecimal.valueOf(-0.000000000001)),
                negativeOrZero("f.BigInteger", BigInteger.valueOf(-1)),
                negativeOrZero("f.Double", -0.00000000001D),
                negativeOrZero("f.Float", -0.000000000001F),
                negativeOrZero("f.Long", -1L),
                negativeOrZero("f.Integer", -1)
        );
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("WHEN is null THEN expect errors")
    void negativeOrZero4() {
        List<Violation> violations = validateAll(
                negativeOrZero("f.BigDecimal", (BigDecimal) null),
                negativeOrZero("f.BigInteger", (BigInteger) null),
                negativeOrZero("f.Double", (Double) null),
                negativeOrZero("f.Float", (Float) null),
                negativeOrZero("f.Long", (Long) null),
                negativeOrZero("f.Integer", (Integer) null)
        );
        violations.forEach(violation -> assertViolationIsRequired(violation, violation.getField()));
    }

}
