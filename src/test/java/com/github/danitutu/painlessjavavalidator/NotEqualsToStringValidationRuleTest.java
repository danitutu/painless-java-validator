package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class NotEqualsToStringValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect violation")
  public void notEqualsTo1() {
    Optional<Violation> violation = ValidationRule.notEqualsToRule("field.path", null, null);

    assertTrue(violation.isPresent());
    assertEquals("field.path", violation.get().getField());
    assertEquals("validation.error.value.is.required", violation.get().getMessage());
    assertEquals("The value is required.", violation.get().getDetails());
  }

  @Test
  @DisplayName("WHEN values are not equal THEN expect no violation")
  public void notEqualsTo2() {
    Optional<Violation> violation = ValidationRule.notEqualsToRule("field.path", "s1", "s2");

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN values are equal THEN expect violation")
  public void notEqualsTo3() {
    Optional<Violation> violation = ValidationRule.notEqualsToRule("field.path", "s1", "s1");

    assertTrue(violation.isPresent());
    assertEquals("field.path", violation.get().getField());
    assertEquals("validation.error.string.is.equal", violation.get().getMessage());
    assertEquals("The value is equal to the other value.", violation.get().getDetails());
  }
}
