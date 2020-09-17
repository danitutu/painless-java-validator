package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsEqual;
import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotEqualsToStringValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect violation")
  void notEqualsTo1() {
    Optional<Violation> violation = ValidationRule.notEqualsToRule("field.path", null, null);

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN values are not equal THEN expect no violation")
  void notEqualsTo2() {
    Optional<Violation> violation = ValidationRule.notEqualsToRule("field.path", "s1", "s2");

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN values are equal THEN expect violation")
  void notEqualsTo3() {
    Optional<Violation> violation = ValidationRule.notEqualsToRule("field.path", "s1", "s1");

    assertTrue(violation.isPresent());
    assertViolationIsEqual(violation.get(), "field.path", "s1");
  }
}
