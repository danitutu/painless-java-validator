package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsNotEqual;
import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EqualsToStringValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect violation")
  void equalsTo1() {
    Optional<Violation> violation = ValidationRule.equalsToRule("field.path", null, null);

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN values are not equal THEN expect violation")
  void equalsTo2() {
    Optional<Violation> violation = ValidationRule.equalsToRule("field.path", "s1", "s2");

    assertTrue(violation.isPresent());
    assertViolationIsNotEqual(violation.get(), "field.path", "s2");
  }

  @Test
  @DisplayName("WHEN values are equal THEN expect no violation")
  void equalsTo3() {
    Optional<Violation> violation = ValidationRule.equalsToRule("field.path", "s1", "s1");

    assertFalse(violation.isPresent());
  }
}
