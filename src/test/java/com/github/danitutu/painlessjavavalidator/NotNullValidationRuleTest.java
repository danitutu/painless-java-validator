package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NotNullValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect violation")
  void notNull1() {
    Optional<Violation> violation = ValidationRule.notNullRule("field.path", null);

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN value is not null THEN expect no violation")
  void notNull2() {
    Optional<Violation> violation = ValidationRule.notNullRule("field.path", new Object());

    assertFalse(violation.isPresent());
  }
}
