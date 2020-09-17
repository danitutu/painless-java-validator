package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationGreaterThanMax;
import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MaxIntegerValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect violation")
  void max1() {
    Optional<Violation> violation = ValidationRule.maxRule("field.path", null, 3);

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN value is equal to max THEN expect no violation")
  void max2() {
    Optional<Violation> violation = ValidationRule.maxRule("field.path", 4, 4);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is greater than max THEN expect violation")
  void max3() {
    Optional<Violation> violation = ValidationRule.maxRule("field.path", 6, 5);

    assertTrue(violation.isPresent());
    assertViolationGreaterThanMax(violation.get(), "field.path", 5);
  }

  @Test
  @DisplayName("WHEN value is smaller than max THEN expect no violation")
  void max4() {
    Optional<Violation> violation = ValidationRule.maxRule("field.path", 3, 5);

    assertFalse(violation.isPresent());
  }
}
