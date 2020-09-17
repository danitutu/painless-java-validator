package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsNotEmpty;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotEmptyValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect violation")
  void notEmpty1() {
    Optional<Violation> violation = ValidationRule.notEmptyRule("field.path", null);

    assertTrue(violation.isPresent());
    assertViolationIsNotEmpty(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN value is empty string THEN expect no violation")
  void notEmpty2() {
    Optional<Violation> violation = ValidationRule.notEmptyRule("field.path", "");

    assertTrue(violation.isPresent());
    assertViolationIsNotEmpty(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN value is filled in THEN expect violation")
  void notEmpty3() {
    Optional<Violation> violation = ValidationRule.notEmptyRule("field.path", "a value");

    assertFalse(violation.isPresent());
  }
}
