package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationSmallerThanMin;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinIntegerValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect no violation")
  void min1() {
    Optional<Violation> violation = ValidationRule.minRule("field.path", null, 3);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is equal to min THEN expect no violation")
  void min2() {
    Optional<Violation> violation = ValidationRule.minRule("field.path", 4, 4);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is smaller than min THEN expect violation")
  void min3() {
    Optional<Violation> violation = ValidationRule.minRule("field.path", 2, 5);

    assertTrue(violation.isPresent());
    assertViolationSmallerThanMin(violation.get(), "field.path", 5);
  }

  @Test
  @DisplayName("WHEN value is greater than min THEN expect no violation")
  void min4() {
    Optional<Violation> violation = ValidationRule.minRule("field.path", 3, 1);

    assertFalse(violation.isPresent());
  }
}
