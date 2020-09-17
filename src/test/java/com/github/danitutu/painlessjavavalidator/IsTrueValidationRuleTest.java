package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IsTrueValidationRuleTest {

  @Test
  @DisplayName("WHEN condition is null THEN expect exception")
  void isTrueRule1() {
    IllegalArgumentException exception =
            assertThrows(
                    IllegalArgumentException.class,
                    () -> ValidationRule.isTrueRule(null, (Violation) null));

    assertEquals("Condition cannot be null", exception.getMessage());
  }

  @Test
  @DisplayName("WHEN violation is null THEN expect exception")
  void isTrueRule2() {
    IllegalArgumentException exception =
            assertThrows(
                    IllegalArgumentException.class,
                    () -> ValidationRule.isTrueRule(() -> true, (Violation) null));

    assertEquals("Violation provider cannot be null", exception.getMessage());
  }

  @Test
  @DisplayName("WHEN condition is evaluated to false THEN expect violation")
  void isTrueRule3() {
    Violation violation = Violation.of(null, null, null, null);

    Optional<Violation> result = ValidationRule.isTrueRule(() -> false, violation);

    assertTrue(result.isPresent());
    assertSame(violation, result.get());
  }

  @Test
  @DisplayName("WHEN condition is evaluated to true THEN expect no violation")
  void isTrueRule4() {
    Violation violation = Violation.of(null, null, null, null);

    Optional<Violation> result = ValidationRule.isTrueRule(() -> true, violation);

    assertFalse(result.isPresent());
  }
}
