package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationNegativeOrZero;
import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateAll;
import static com.github.danitutu.painlessjavavalidator.ValidationRule.positive;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PositiveValidationRuleTest {

    @Test
    @DisplayName("WHEN is positive THEN expect no violation")
    void positive1() {
        List<Violation> violations = validateAll(
                positive("f.BigDecimal", BigDecimal.valueOf(0.000000000001)),
                positive("f.BigInteger", BigInteger.valueOf(1)),
                positive("f.Double", 0.000000000001D),
                positive("f.Float", 0.000000000001F),
                positive("f.Long", 1L),
                positive("f.Integer", 1)
        );
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("WHEN is zero THEN expect errors")
    void positive2() {
        List<Violation> violations = validateAll(
                positive("f.BigDecimal", BigDecimal.ZERO),
                positive("f.BigInteger", BigInteger.ZERO),
                positive("f.Double", 0.00000000000D),
                positive("f.Float", 0.000000000000F),
                positive("f.Long", 0L),
                positive("f.Integer", 0)
        );
        violations.forEach(violation -> assertViolationNegativeOrZero(violation, violation.getField()));
    }

    @Test
    @DisplayName("WHEN is negative THEN expect errors")
    void positive3() {
        List<Violation> violations = validateAll(
                positive("f.BigDecimal", BigDecimal.valueOf(-0.000000000001)),
                positive("f.BigInteger", BigInteger.valueOf(-1)),
                positive("f.Double", -0.00000000001D),
                positive("f.Float", -0.000000000001F),
                positive("f.Long", -1L),
                positive("f.Integer", -1)
        );
        violations.forEach(violation -> assertViolationNegativeOrZero(violation, violation.getField()));
    }

    @Test
    @DisplayName("WHEN is null THEN expect errors")
    void positive4() {
        List<Violation> violations = validateAll(
                positive("f.BigDecimal", (BigDecimal) null),
                positive("f.BigInteger", (BigInteger) null),
                positive("f.Double", (Double) null),
                positive("f.Float", (Float) null),
                positive("f.Long", (Long) null),
                positive("f.Integer", (Integer) null)
        );
        violations.forEach(violation -> assertViolationIsRequired(violation, violation.getField()));
    }

}
