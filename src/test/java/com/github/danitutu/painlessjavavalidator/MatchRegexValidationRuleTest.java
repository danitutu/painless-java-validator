package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationRegexNoMatch;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MatchRegexValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect violation")
  void matchRegex1() {
    Optional<Violation> violation = ValidationRule.matchRegexRule("field.path", null, "[A-Z0-9]+");

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN value is not matching the regex exp THEN expect violation")
  void matchRegex2() {
    Optional<Violation> violation =
            ValidationRule.matchRegexRule("field.path", "TEST1234", "[A-Z]+");

    assertTrue(violation.isPresent());
    assertViolationRegexNoMatch(violation.get(), "field.path", "[A-Z]+");
  }

  @Test
  @DisplayName("WHEN value is matching the regex exp THEN expect no violation")
  void matchRegex3() {
    Optional<Violation> violation =
            ValidationRule.matchRegexRule("field.path", "TEST1234", "[A-Z0-9]+");

    assertFalse(violation.isPresent());
  }
}
