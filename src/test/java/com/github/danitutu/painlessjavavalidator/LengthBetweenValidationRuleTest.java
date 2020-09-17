package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsRequired;
import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationLengthNotBetween;
import static org.junit.jupiter.api.Assertions.*;

class LengthBetweenValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect violation")
  void lengthBetween1() {
    Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", null, 3, 10);

    assertTrue(violation.isPresent());
    assertViolationIsRequired(violation.get(), "field.path");
  }

  @Test
  @DisplayName(
          "WHEN value is empty string and the limit is equal to inferior limit THEN expect no violations")
  void lengthBetween2() {
    Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "", 0, 10);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName(
          "WHEN value is empty string and the value length is outside interval THEN expect violation")
  void lengthBetween12() {
    Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "", 1, 10);

    assertTrue(violation.isPresent());
    assertViolationLengthNotBetween(violation.get(), "field.path", 1, 10);
  }

  @Test
  @DisplayName("WHEN value length is equal to min THEN expect no violation")
  void lengthBetween4() {
    Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 4, 10);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value length is equal to max THEN expect no violation")
  void lengthBetween5() {
    Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 2, 4);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value length is between min and max THEN expect no violation")
  void lengthBetween6() {
    Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 2, 5);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value length is smaller than min THEN expect violation")
  void lengthBetween7() {
    Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 5, 7);

    assertTrue(violation.isPresent());
    assertViolationLengthNotBetween(violation.get(), "field.path", 5, 7);
  }

  @Test
  @DisplayName("WHEN value length is greater than max THEN expect violation")
  void lengthBetween8() {
    Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 1, 3);

    assertTrue(violation.isPresent());
    assertViolationLengthNotBetween(violation.get(), "field.path", 1, 3);
  }

  @Test
  @DisplayName("WHEN min greater than max THEN exception")
  void lengthBetween9() {
    IllegalArgumentException ex =
            assertThrows(
                    IllegalArgumentException.class,
                    () -> ValidationRule.lengthBetweenRule("field.path", "test", 3, 2));

    assertEquals("min is greater than max", ex.getMessage());
  }

  @Test
  @DisplayName("WHEN value length different than min and min=max THEN expect violation")
  void lengthBetween10() {
    Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 3, 3);

    assertTrue(violation.isPresent());
    assertViolationLengthNotBetween(violation.get(), "field.path", 3, 3);
  }

  @Test
  @DisplayName("WHEN value length=min=max THEN expect no violation")
  void lengthBetween11() {
    Optional<Violation> violation = ValidationRule.lengthBetweenRule("field.path", "test", 4, 4);

    assertFalse(violation.isPresent());
  }
}
