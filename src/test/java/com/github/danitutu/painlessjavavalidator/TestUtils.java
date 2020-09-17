package com.github.danitutu.painlessjavavalidator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TestUtils {

  private TestUtils() {
  }

  public static void assertViolationIsNotBlank(Violation violation, String field) {
    assertViolation(violation, field, "validation.error.value.is.not.blank", "The value is empty.");
    assertNull(violation.getAttributes());
  }

  public static void assertViolation(
          Violation violation, String field, String message, String details) {
    assertEquals(field, violation.getField());
    assertEquals(message, violation.getMessage());
    assertEquals(details, violation.getDetails());
  }

  public static void assertViolationIsNotNull(Violation violation, String field) {
    assertViolation(
            violation, field, "validation.error.value.is.not.null", "The value is not null.");
    assertNull(violation.getAttributes());
  }

  public static void assertViolationIsNotEmpty(Violation violation, String field) {
    assertViolation(
            violation, field, "validation.error.value.is.not.empty", "The value is not empty.");
    assertNull(violation.getAttributes());
  }

  public static void assertViolationIsRequired(Violation violation, String field) {
    assertViolation(
            violation, field, "validation.error.value.is.required", "The value is required.");
    assertNull(violation.getAttributes());
  }

  public static void assertViolationStrictPositive(Violation violation, String field) {
    assertViolation(
            violation,
            field,
            "validation.error.negative.value.or.zero",
            "The value must be a strict positive number");
    assertNull(violation.getAttributes());
  }

  public static void assertViolationStrictNegative(Violation violation, String field) {
    assertViolation(
            violation, field, "validation.error.negative.value", "The value must be a positive number");
    assertNull(violation.getAttributes());
  }

  public static void assertViolationIsBefore(Violation violation, String field, String other) {
    assertViolationWithOneAttribute(
            violation,
            field,
            "validation.error.value.is.before",
            "The value is before the other value.",
            other,
            "other");
  }

  public static void assertViolationWithOneAttribute(
          Violation violation,
          String field,
          String message,
          String details,
          Object value,
          String attributeName) {
    assertViolation(violation, field, message, details);
    assertEquals(1, violation.getAttributes().size());
    assertEquals(value, violation.getAttributes().get(attributeName));
  }

  public static void assertViolationIsBeforeOrEqual(
          Violation violation, String field, Object other) {
    assertViolationWithOneAttribute(
            violation,
            field,
            "validation.error.value.is.before.or.equal",
            "The value is before or equal the other value.",
            other,
            "other");
  }

  public static void assertViolationRegexNoMatch(Violation violation, String field, String regex) {
    assertViolationWithOneAttribute(
            violation,
            field,
            "validation.error.string.value.regex.no.match",
            "Value does not match the expected regex.",
            regex,
            "regexPattern");
  }

  public static void assertViolationGreaterThanMax(Violation violation, String field, int max) {
    assertViolationWithOneAttribute(
            violation,
            field,
            "validation.error.integer.value.greater.than.max",
            "Value is greater than max.",
            max,
            "max");
  }

  public static void assertViolationSmallerThanMin(Violation violation, String field, int min) {
    assertViolationWithOneAttribute(
            violation,
            field,
            "validation.error.integer.value.smaller.than.min",
            "Value is smaller than min.",
            min,
            "min");
  }

  public static void assertViolationIsNotEqual(Violation violation, String field, Object other) {
    assertViolationWithOneAttribute(
            violation,
            field,
            "validation.error.value.is.not.equal",
            "The value is not equal to the other value.",
            other,
            "other");
  }

  public static void assertViolationIsEqual(Violation violation, String field, Object other) {
    assertViolationWithOneAttribute(
            violation,
            field,
            "validation.error.string.is.equal",
            "The value is equal to the other value.",
            other,
            "other");
  }

  public static void assertViolationIsAfter(Violation violation, String field, Object other) {
    assertViolationWithOneAttribute(
            violation,
            field,
            "validation.error.value.is.after",
            "The value is after the other value.",
            other,
            "other");
  }

  public static void assertViolationIsAfterOrEqual(
          Violation violation, String field, Object other) {
    assertViolationWithOneAttribute(
            violation,
            field,
            "validation.error.value.is.after.or.equal",
            "The value is after or equal the other value.",
            other,
            "other");
  }

  public static void assertViolationIntegerNotInRange(
          Violation violation, String field, int firstValue, int secondValue) {
    assertViolationWithTwoAttributes(
            violation,
            field,
            "validation.error.integer.value.not.in.range",
            "Value is not in range.",
            firstValue,
            secondValue,
            "min",
            "max");
  }

  public static void assertViolationWithTwoAttributes(
          Violation violation,
          String field,
          String message,
          String details,
          Object firstValue,
          Object secondValue,
          String firstAttributeName,
          String secondAttributeName) {
    assertViolation(violation, field, message, details);
    assertEquals(2, violation.getAttributes().size());
    assertEquals(firstValue, violation.getAttributes().get(firstAttributeName));
    assertEquals(secondValue, violation.getAttributes().get(secondAttributeName));
  }

  public static void assertViolationLengthNotBetween(
          Violation violation, String field, int min, int max) {
    assertViolationWithTwoAttributes(
            violation,
            field,
            "validation.error.string.value.not.between",
            "Value is not in range.",
            min,
            max,
            "min",
            "max");
  }
}
