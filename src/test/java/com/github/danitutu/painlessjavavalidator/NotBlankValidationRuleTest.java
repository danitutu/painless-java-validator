package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotBlankValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect violation")
  void notBlank1() {
    Optional<Violation> violation = ValidationRule.notBlankRule("field.path", null);

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN value is empty string THEN expect violation")
  void notBlank2() {
    Optional<Violation> violation = ValidationRule.notBlankRule("field.path", "");

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN value is filled in THEN expect no violation")
  void notBlank3() {
    Optional<Violation> violation = ValidationRule.notBlankRule("field.path", "a value");

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value has multiple spaces THEN expect violation")
  void notBlank4() {
    Optional<Violation> violation = ValidationRule.notBlankRule("field.path", "   ");

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }
}
