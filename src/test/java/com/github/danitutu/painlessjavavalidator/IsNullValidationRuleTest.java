package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IsNullValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect no violation")
  void isNull1() {
    Optional<Violation> violation = ValidationRule.isNullRule("field.path", null);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is not null THEN expect violation")
  void isNull2() {
    Optional<Violation> violation = ValidationRule.isNullRule("field.path", new Object());

    assertTrue(violation.isPresent());
    assertViolationIsNotNull(violation.get(), "field.path");
  }
}
