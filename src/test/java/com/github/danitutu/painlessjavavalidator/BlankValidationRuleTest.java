package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BlankValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect no violation")
  void blank1() {
    Optional<Violation> violation = ValidationRule.blankRule("field.path", null);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is empty string THEN expect no violation")
  void blank2() {
    Optional<Violation> violation = ValidationRule.blankRule("field.path", "");

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is filled in THEN expect violation")
  void blank3() {
    Optional<Violation> violation = ValidationRule.blankRule("field.path", "a value");

    assertTrue(violation.isPresent());
    TestUtils.assertViolationIsNotBlank(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN value has only spaces THEN expect no violation")
  void blank4() {
    Optional<Violation> violation = ValidationRule.blankRule("field.path", "   ");

    assertFalse(violation.isPresent());
  }
}
