package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateAllAndStopIfViolations;
import static com.github.danitutu.painlessjavavalidator.ValidationRule.*;
import static org.junit.jupiter.api.Assertions.*;

class ValidationEngineTest {

  @Test
  @DisplayName("WHEN all return violations THEN expect all violations")
  void validateAll1() {
    ViolationProvider[] validationRules = {
            notNull("notNullCheck", null),
            notBlank("notBlankCheck1", "   "),
            lengthBetween("lengthBetweenCheck", "a string", 10, 20),
            matchRegex("regexCheck", "a", "[0-9]+"),
            inRange("rangeCheck", 5, 6, 7),
            min("minCheck", 5, 6),
            max("maxCheck", 6, 5),
            after("isAfterCheck", 5, 5),
            afterOrEqualsTo("isAfterOrEqualsToCheck", 4, 5),
            before("isBeforeCheck", 5, 5),
            beforeOrEqualsTo("isBeforeOrEqualsToCheck", 6, 5),
            equalsTo("equalsToStringCheck", "s1", "s2"),
            equalsTo("equalsToComparableCheck", 5, 6),
            notEqualsTo("notEqualsToStringCheck", "s1", "s1"),
            compareStrings(
                    "compareStringsCheck",
                    "s1",
                    "s2",
                    (s, s2) -> !s.equals(s2),
                    () -> Violation.of("compareStringsCheck", null, null, null)),
            compareComparable(
                    "compareComparablesCheck",
                    1,
                    2,
                    (s, s2) -> !s.equals(s2),
                    () -> Violation.of("compareComparablesCheck", null, null, null)),
            isNull("nullCheck", new Object()),
            empty("emptyCheck", "s"),
            notEmpty("notEmptyCheck", ""),
            blank("blankCheck", "s"),
            notBlank("notBlankCheck2", ""),
            notBlank("notBlankCheck3", null),
            isTrue(() -> false, Violation.of("isTrueCheck", null, null)),
            isFalse(() -> true, Violation.of("isFalseCheck", null, null)),
    };

    List<Violation> violations = ValidationEngine.validateAll(validationRules);

    assertEquals("notNullCheck", violations.get(0).getField());
    assertEquals("notBlankCheck1", violations.get(1).getField());
    assertEquals("lengthBetweenCheck", violations.get(2).getField());
    assertEquals("regexCheck", violations.get(3).getField());
    assertEquals("rangeCheck", violations.get(4).getField());
    assertEquals("minCheck", violations.get(5).getField());
    assertEquals("maxCheck", violations.get(6).getField());
    assertEquals("isAfterCheck", violations.get(7).getField());
    assertEquals("isAfterOrEqualsToCheck", violations.get(8).getField());
    assertEquals("isBeforeCheck", violations.get(9).getField());
    assertEquals("isBeforeOrEqualsToCheck", violations.get(10).getField());
    assertEquals("equalsToStringCheck", violations.get(11).getField());
    assertEquals("equalsToComparableCheck", violations.get(12).getField());
    assertEquals("notEqualsToStringCheck", violations.get(13).getField());
    assertEquals("compareStringsCheck", violations.get(14).getField());
    assertEquals("compareComparablesCheck", violations.get(15).getField());
    assertEquals("nullCheck", violations.get(16).getField());
    assertEquals("emptyCheck", violations.get(17).getField());
    assertEquals("notEmptyCheck", violations.get(18).getField());
    assertEquals("blankCheck", violations.get(19).getField());
    assertEquals("notBlankCheck2", violations.get(20).getField());
    assertEquals("notBlankCheck3", violations.get(21).getField());
    assertEquals("isTrueCheck", violations.get(22).getField());
    assertEquals("isFalseCheck", violations.get(23).getField());
  }

  @Test
  @DisplayName("WHEN all pass THEN expect no violation")
  void validateAll2() {
    ViolationProvider[] validationRules = {
            notNull("notNullCheck", new Object()),
            notBlank("notBlankCheck", "s"),
            lengthBetween("lengthBetweenCheck", "a string", 0, 20),
            matchRegex("regexCheck", "a", "[a-z]+"),
            inRange("rangeCheck", 6, 6, 7),
            min("minCheck", 6, 6),
            max("maxCheck", 5, 5),
            after("isAfterCheck", 6, 5),
            afterOrEqualsTo("isAfterOrEqualsToCheck", 5, 5),
            before("isBeforeCheck", 4, 5),
            beforeOrEqualsTo("isBeforeOrEqualsToCheck", 5, 5),
            equalsTo("equalsToStringCheck", "s1", "s1"),
            equalsTo("equalsToComparableCheck", 6, 6),
            notEqualsTo("notEqualsToStringCheck", "s1", "s2"),
            compareStrings(
                    "compareStringsCheck",
                    "s1",
                    "s1",
                    (s, s2) -> !s.equals(s2),
                    () -> Violation.of("compareStringsCheck", null, null, null)),
            compareComparable(
                    "compareComparablesCheck",
                    1,
                    1,
                    (s, s2) -> !s.equals(s2),
                    () -> Violation.of("compareComparablesCheck", null, null, null)),
            isNull("nullCheck", null),
            empty("emptyCheck", ""),
            blank("blankCheck1", ""),
            blank("blankCheck2", "  "),
            blank("blankCheck3", null),
            isTrue(() -> true, Violation.of("isTrueCheck", null, null)),
            isFalse(() -> false, Violation.of("isFalseCheck", null, null)),
    };

    List<Violation> violations = ValidationEngine.validateAll(validationRules);

    assertEquals(0, violations.size());
  }

  @Test
  @DisplayName("WHEN first rule fails and the last rule fails too THEN expect two violation")
  void validateAll3() {
    ViolationProvider[] validationRules = {
            notNull("notNullCheck", null),
            notBlank("notBlankCheck", "s"),
            lengthBetween("lengthBetweenCheck", "a string", 0, 20),
            matchRegex("regexCheck", "a", "[a-z]+"),
            inRange("rangeCheck", 6, 6, 7),
            min("minCheck", 6, 6),
            max("maxCheck", 5, 5),
            after("isAfterCheck", 6, 5),
            afterOrEqualsTo("isAfterOrEqualsToCheck", 5, 5),
            before("isBeforeCheck", 4, 5),
            beforeOrEqualsTo("isBeforeOrEqualsToCheck", 5, 5),
            equalsTo("equalsToStringCheck", "s1", "s1"),
            equalsTo("equalsToComparableCheck", 6, 6),
            notEqualsTo("notEqualsToStringCheck", "s1", "s2"),
            compareStrings(
                    "compareStringsCheck",
                    "s1",
                    "s1",
                    (s, s2) -> !s.equals(s2),
                    () -> Violation.of("compareStringsCheck", null, null, null)),
            compareComparable(
                    "compareComparablesCheck",
                    1,
                    2,
                    (s, s2) -> !s.equals(s2),
                    () -> Violation.of("compareComparablesCheck", null, null, null))
    };

    List<Violation> violations = ValidationEngine.validateAll(validationRules);

    assertEquals(2, violations.size());
    assertEquals("notNullCheck", violations.get(0).getField());
    assertEquals("compareComparablesCheck", violations.get(1).getField());
  }

  @Test
  @DisplayName("WHEN input is null THEN expect no violation")
  void validateAll4() {
    List<Violation> violations = ValidationEngine.validateAll((ViolationProvider[]) null);

    assertEquals(0, violations.size());
  }

  @Test
  @DisplayName("WHEN violations THEN expect exception")
  void validateAllAndStopIfViolations1() {
    Violation violation = Violation.of(null, null, null, null);

    ValidationException exception =
            assertThrows(
                    ValidationException.class,
                    () -> validateAllAndStopIfViolations(() -> Optional.of(violation)));

    assertSame(violation, exception.getViolations().get(0));
  }

  @Test
  @DisplayName("WHEN no violations THEN expect no exception")
  void validateAllAndStopIfViolations2() {
    validateAllAndStopIfViolations(Optional::empty);
  }

  @Test
  @DisplayName("WHEN no violations (null) THEN expect no exception")
  void validateAllAndStopIfViolations3() {
    validateAllAndStopIfViolations((ViolationProvider[]) null);
  }

  @Test
  @DisplayName("WHEN all rules fail THEN expect only the first violation")
  void validateFindFirst1() {
    ViolationProvider[] validationRules = {
            notNull("notNullCheck", null), notBlank("notBlankCheck", ""),
    };

    List<Violation> violations = ValidationEngine.validateFindFirst(validationRules);

    assertEquals(1, violations.size());
    assertEquals("notNullCheck", violations.get(0).getField());
  }

  @Test
  @DisplayName("WHEN all rules pass THEN expect no violations")
  void validateFindFirst2() {
    ViolationProvider[] validationRules = {
            notNull("notNullCheck", new Object()),
            notBlank("notBlankCheck", "s"),
    };

    List<Violation> violations = ValidationEngine.validateFindFirst(validationRules);

    assertEquals(0, violations.size());
  }

  @Test
  @DisplayName(
          "WHEN first rule pass, second rule fail, third rule fail THEN expect only the second violation")
  void validateFindFirst3() {
    ViolationProvider[] validationRules = {
            notNull("notNullCheck", new Object()),
            notBlank("notBlankCheck", ""),
            notNull("notBlankCheck", null),
    };

    List<Violation> violations = ValidationEngine.validateFindFirst(validationRules);

    assertEquals(1, violations.size());
    assertEquals("notBlankCheck", violations.get(0).getField());
  }

  @Test
  @DisplayName("WHEN input is null THEN expect no violation")
  void validateFindFirst4() {
    List<Violation> violations = ValidationEngine.validateFindFirst((ViolationProvider[]) null);

    assertEquals(0, violations.size());
  }

  @Test
  @DisplayName("WHEN first rule fails THEN throw exception with the violation ")
  void validateFindFirstAndStopIfViolation1() {
    ViolationProvider[] validationRules = {
            notNull("notNullCheck", null), notBlank("notBlankCheck", ""),
    };

    ValidationException exception =
            assertThrows(
                    ValidationException.class,
                    () -> ValidationEngine.validateFindFirstAndStopIfViolation(validationRules));

    assertEquals(1, exception.getViolations().size());
    assertEquals("notNullCheck", exception.getViolations().get(0).getField());
  }

  @Test
  @DisplayName("WHEN no validations fail THEN expect no validation ")
  void validateFindFirstAndStopIfViolation2() {
    ViolationProvider[] validationRules = {
            notNull("notNullCheck", new Object()),
    };

    ValidationEngine.validateFindFirstAndStopIfViolation(validationRules);
  }

  @Test
  @DisplayName("WHEN input is null THEN expect no validation ")
  void validateFindFirstAndStopIfViolation3() {
    ValidationEngine.validateFindFirstAndStopIfViolation((ViolationProvider[]) null);
  }
}
