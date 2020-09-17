package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIntegerNotInRange;
import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static org.junit.jupiter.api.Assertions.*;

class InRangeIntegerValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect violation")
  void inRange1() {
    Optional<Violation> violation = ValidationRule.inRangeRule("field.path", null, 3, 10);

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }

  @Test
  @DisplayName("WHEN value is equal to min THEN expect no violation")
  void inRange2() {
    Optional<Violation> violation = ValidationRule.inRangeRule("field.path", 4, 4, 10);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is equal to max THEN expect no violation")
  void inRange3() {
    Optional<Violation> violation = ValidationRule.inRangeRule("field.path", 4, 2, 4);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is between min and max THEN expect no violation")
  void inRange4() {
    Optional<Violation> violation = ValidationRule.inRangeRule("field.path", 4, 2, 5);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is smaller than min THEN expect violation")
  void inRange5() {
    Optional<Violation> violation = ValidationRule.inRangeRule("field.path", 3, 5, 7);

    assertTrue(violation.isPresent());
    assertViolationIntegerNotInRange(violation.get(), "field.path", 5, 7);
  }

  @Test
  @DisplayName("WHEN value is greater than max THEN expect violation")
  void inRange6() {
    Optional<Violation> violation = ValidationRule.inRangeRule("field.path", 5, 1, 3);

    assertTrue(violation.isPresent());
    assertViolationIntegerNotInRange(violation.get(), "field.path", 1, 3);
  }

  @Test
  @DisplayName("WHEN min greater than max THEN exception")
  void inRange7() {
    IllegalArgumentException ex =
            assertThrows(
                    IllegalArgumentException.class,
                    () -> ValidationRule.inRangeRule("field.path", 0, 3, 2));

    assertEquals("min is greater than max", ex.getMessage());
  }

  @Test
  @DisplayName("WHEN value different than min and min=max THEN expect violation")
  void inRange8() {
    Optional<Violation> violation = ValidationRule.inRangeRule("field.path", 2, 3, 3);

    assertTrue(violation.isPresent());
    assertViolationIntegerNotInRange(violation.get(), "field.path", 3, 3);
  }

  @Test
  @DisplayName("WHEN value=min=max THEN expect no violation")
  void inRange9() {
    Optional<Violation> violation = ValidationRule.inRangeRule("field.path", 4, 4, 4);

    assertFalse(violation.isPresent());
  }
}
