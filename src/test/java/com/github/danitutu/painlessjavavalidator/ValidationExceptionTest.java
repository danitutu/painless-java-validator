package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationWithOneAttribute;
import static java.util.Collections.*;
import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

  @Test
  @DisplayName(
          "WHEN instantiating using a single Violation object THEN return the violation object with its input data")
  void getViolations1() {
    ValidationException exception =
            new ValidationException(
                    Violation.of("field.path", "message", "details", singletonMap("other", 1)));
    Violation violation = exception.getViolations().get(0);
    assertViolationWithOneAttribute(violation, "field.path", "message", "details", 1, "other");
  }

  @Test
  @DisplayName(
          "WHEN instantiating using a list of violation objects THEN return the violation objects with their input data")
  void getViolations2() {
    ValidationException exception =
            new ValidationException(
                    Arrays.asList(
                            Violation.of("field.path1", "message1", "details1", singletonMap("other1", 1)),
                            Violation.of("field.path2", "message2", "details2", singletonMap("other2", 2))));
    Violation violation1 = exception.getViolations().get(0);
    assertViolationWithOneAttribute(violation1, "field.path1", "message1", "details1", 1, "other1");

    Violation violation2 = exception.getViolations().get(1);
    assertViolationWithOneAttribute(violation2, "field.path2", "message2", "details2", 2, "other2");

    assertEquals(
            "Violations: "
                    + "["
                    + "{field='field.path1', message='message1', details='details1', attributes={other1=1}}, "
                    + "{field='field.path2', message='message2', details='details2', attributes={other2=2}}"
                    + "]",
            exception.getMessage());
  }

  @Test
  @DisplayName("WHEN violations is null THEN return only brackets")
  void getViolations3() {
    ValidationException exception = new ValidationException((List<Violation>) null);

    assertEquals(
            "Violations: []",
            exception.getMessage());
  }

  @Test
  @DisplayName("WHEN violations present THEN expect exception")
  void stopIfViolations1() {
    List<Violation> violations = singletonList(Violation.of(null, null, null, null));
    ValidationException exception =
            assertThrows(
                    ValidationException.class, () -> ValidationException.stopIfViolations(violations));
    assertSame(violations, exception.getViolations());
  }

  @Test
  @DisplayName("WHEN violations is empty THEN expect no exception")
  void stopIfViolations2() {
    ValidationException.stopIfViolations(emptyList());
  }

  @Test
  @DisplayName("WHEN violations is null THEN expect no exception")
  void stopIfViolations3() {
    ValidationException.stopIfViolations(null);
  }

  @Test
  @DisplayName("WHEN violation present THEN expect exception")
  void stopIfViolation1() {
    Violation violation = Violation.of(null, null, null, null);
    ValidationException exception =
            assertThrows(
                    ValidationException.class, () -> ValidationException.stopIfViolation(violation));
    assertSame(violation, exception.getViolations().get(0));
    assertEquals(1, exception.getViolations().size());
  }

  @Test
  @DisplayName("WHEN violations is null THEN expect no exception")
  void stopIfViolation2() {
    ValidationException.stopIfViolation(null);
  }
}
