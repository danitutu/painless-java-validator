package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class IsNullValidationRuleTest {

  @Test
  @DisplayName("WHEN value is null THEN expect no violation")
  public void isNull1() {
    Optional<Violation> violation = ValidationRule.isNullRule("field.path", null);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN value is not null THEN expect violation")
  public void isNull2() {
    Optional<Violation> violation = ValidationRule.isNullRule("field.path", new Object());

    assertTrue(violation.isPresent());
    assertEquals("field.path", violation.get().getField());
    assertEquals("validation.error.value.is.not.null", violation.get().getMessage());
    assertEquals("The value is not null.", violation.get().getDetails());
    assertNull(violation.get().getAttributes());
  }
}
