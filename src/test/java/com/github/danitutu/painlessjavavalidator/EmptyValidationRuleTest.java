package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EmptyValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect no violation")
  void empty1() {
    Optional<Violation> violation = ValidationRule.emptyRule("field.path", null);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is empty string THEN expect no violation")
  void empty2() {
    Optional<Violation> violation = ValidationRule.emptyRule("field.path", "");

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is filled in THEN expect violation")
  void empty3() {
    Optional<Violation> violation = ValidationRule.emptyRule("field.path", "a value");

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }
}
