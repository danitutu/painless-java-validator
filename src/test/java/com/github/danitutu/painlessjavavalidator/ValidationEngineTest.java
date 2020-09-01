package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.ValidationEngine.validateAllAndStopIfViolations;
import static org.junit.jupiter.api.Assertions.*;

class ValidationEngineTest {

    @Test
    @DisplayName("WHEN all return violations THEN expect all violations")
    void validateAll1() {
        ViolationProvider[] validationRules = {
                ValidationRule.notNull("notNullCheck", null),
                ValidationRule.notBlank("notBlankCheck1", "   "),
                ValidationRule.lengthBetween("lengthBetweenCheck", "a string", 10, 20),
                ValidationRule.matchRegex("regexCheck", "a", "[0-9]+"),
                ValidationRule.inRange("rangeCheck", 5, 6, 7),
                ValidationRule.min("minCheck", 5, 6),
                ValidationRule.max("maxCheck", 6, 5),
                ValidationRule.after("isAfterCheck", 5, 5),
                ValidationRule.afterOrEqualsTo("isAfterOrEqualsToCheck", 4, 5),
                ValidationRule.before("isBeforeCheck", 5, 5),
                ValidationRule.beforeOrEqualsTo("isBeforeOrEqualsToCheck", 6, 5),
                ValidationRule.equalsTo("equalsToStringCheck", "s1", "s2"),
                ValidationRule.equalsTo("equalsToComparableCheck", 5, 6),
                ValidationRule.notEqualsTo("notEqualsToStringCheck", "s1", "s1"),
                ValidationRule.compareStrings("compareStringsCheck",
                        "s1",
                        "s2",
                        (s, s2) -> !s.equals(s2),
                        () -> Violation.of("compareStringsCheck", null, null, null)),
                ValidationRule.compareComparable("compareComparablesCheck",
                        1,
                        2,
                        (s, s2) -> !s.equals(s2),
                        () -> Violation.of("compareComparablesCheck", null, null, null)),
                ValidationRule.isNull("nullCheck", new Object()),
                ValidationRule.empty("emptyCheck", "s"),
                ValidationRule.notEmpty("notEmptyCheck", ""),
                ValidationRule.blank("blankCheck", "s"),
                ValidationRule.notBlank("notBlankCheck2", ""),
                ValidationRule.notBlank("notBlankCheck3", null),
                ValidationRule.isTrue(() -> false, Violation.of("isTrueCheck", null, null)),
                ValidationRule.isFalse(() -> true, Violation.of("isFalseCheck", null, null)),
        };

        List<Violation> violations = ValidationEngine.validateAll(validationRules);

        assertEquals("notNullCheck", violations.get(0).getFieldPath());
        assertEquals("notBlankCheck1", violations.get(1).getFieldPath());
        assertEquals("lengthBetweenCheck", violations.get(2).getFieldPath());
        assertEquals("regexCheck", violations.get(3).getFieldPath());
        assertEquals("rangeCheck", violations.get(4).getFieldPath());
        assertEquals("minCheck", violations.get(5).getFieldPath());
        assertEquals("maxCheck", violations.get(6).getFieldPath());
        assertEquals("isAfterCheck", violations.get(7).getFieldPath());
        assertEquals("isAfterOrEqualsToCheck", violations.get(8).getFieldPath());
        assertEquals("isBeforeCheck", violations.get(9).getFieldPath());
        assertEquals("isBeforeOrEqualsToCheck", violations.get(10).getFieldPath());
        assertEquals("equalsToStringCheck", violations.get(11).getFieldPath());
        assertEquals("equalsToComparableCheck", violations.get(12).getFieldPath());
        assertEquals("notEqualsToStringCheck", violations.get(13).getFieldPath());
        assertEquals("compareStringsCheck", violations.get(14).getFieldPath());
        assertEquals("compareComparablesCheck", violations.get(15).getFieldPath());
        assertEquals("nullCheck", violations.get(16).getFieldPath());
        assertEquals("emptyCheck", violations.get(17).getFieldPath());
        assertEquals("notEmptyCheck", violations.get(18).getFieldPath());
        assertEquals("blankCheck", violations.get(19).getFieldPath());
        assertEquals("notBlankCheck2", violations.get(20).getFieldPath());
        assertEquals("notBlankCheck3", violations.get(21).getFieldPath());
        assertEquals("isTrueCheck", violations.get(22).getFieldPath());
        assertEquals("isFalseCheck", violations.get(23).getFieldPath());
    }

    @Test
    @DisplayName("WHEN all pass THEN expect no violation")
    void validateAll2() {
        ViolationProvider[] validationRules = {
                ValidationRule.notNull("notNullCheck", new Object()),
                ValidationRule.notBlank("notBlankCheck", "s"),
                ValidationRule.lengthBetween("lengthBetweenCheck", "a string", 0, 20),
                ValidationRule.matchRegex("regexCheck", "a", "[a-z]+"),
                ValidationRule.inRange("rangeCheck", 6, 6, 7),
                ValidationRule.min("minCheck", 6, 6),
                ValidationRule.max("maxCheck", 5, 5),
                ValidationRule.after("isAfterCheck", 6, 5),
                ValidationRule.afterOrEqualsTo("isAfterOrEqualsToCheck", 5, 5),
                ValidationRule.before("isBeforeCheck", 4, 5),
                ValidationRule.beforeOrEqualsTo("isBeforeOrEqualsToCheck", 5, 5),
                ValidationRule.equalsTo("equalsToStringCheck", "s1", "s1"),
                ValidationRule.equalsTo("equalsToComparableCheck", 6, 6),
                ValidationRule.notEqualsTo("notEqualsToStringCheck", "s1", "s2"),
                ValidationRule.compareStrings("compareStringsCheck",
                        "s1",
                        "s1",
                        (s, s2) -> !s.equals(s2),
                        () -> Violation.of("compareStringsCheck", null, null, null)),
                ValidationRule.compareComparable("compareComparablesCheck",
                        1,
                        1,
                        (s, s2) -> !s.equals(s2),
                        () -> Violation.of("compareComparablesCheck", null, null, null)),
                ValidationRule.isNull("nullCheck", null),
                ValidationRule.empty("emptyCheck", ""),
                ValidationRule.blank("blankCheck1", ""),
                ValidationRule.blank("blankCheck2", "  "),
                ValidationRule.blank("blankCheck3", null),
                ValidationRule.isTrue(() -> true, Violation.of("isTrueCheck", null, null)),
                ValidationRule.isFalse(() -> false, Violation.of("isFalseCheck", null, null)),
        };

        List<Violation> violations = ValidationEngine.validateAll(validationRules);

        assertEquals(0, violations.size());
    }

    @Test
    @DisplayName("WHEN first rule fails and the last rule fails too THEN expect two violation")
    void validateAll3() {
        ViolationProvider[] validationRules = {
                ValidationRule.notNull("notNullCheck", null),
                ValidationRule.notBlank("notBlankCheck", "s"),
                ValidationRule.lengthBetween("lengthBetweenCheck", "a string", 0, 20),
                ValidationRule.matchRegex("regexCheck", "a", "[a-z]+"),
                ValidationRule.inRange("rangeCheck", 6, 6, 7),
                ValidationRule.min("minCheck", 6, 6),
                ValidationRule.max("maxCheck", 5, 5),
                ValidationRule.after("isAfterCheck", 6, 5),
                ValidationRule.afterOrEqualsTo("isAfterOrEqualsToCheck", 5, 5),
                ValidationRule.before("isBeforeCheck", 4, 5),
                ValidationRule.beforeOrEqualsTo("isBeforeOrEqualsToCheck", 5, 5),
                ValidationRule.equalsTo("equalsToStringCheck", "s1", "s1"),
                ValidationRule.equalsTo("equalsToComparableCheck", 6, 6),
                ValidationRule.notEqualsTo("notEqualsToStringCheck", "s1", "s2"),
                ValidationRule.compareStrings("compareStringsCheck",
                        "s1",
                        "s1",
                        (s, s2) -> !s.equals(s2),
                        () -> Violation.of("compareStringsCheck", null, null, null)),
                ValidationRule.compareComparable("compareComparablesCheck",
                        1,
                        2,
                        (s, s2) -> !s.equals(s2),
                        () -> Violation.of("compareComparablesCheck", null, null, null))
        };

        List<Violation> violations = ValidationEngine.validateAll(validationRules);

        assertEquals(2, violations.size());
        assertEquals("notNullCheck", violations.get(0).getFieldPath());
        assertEquals("compareComparablesCheck", violations.get(1).getFieldPath());
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

        ValidationException exception = assertThrows(ValidationException.class, () -> validateAllAndStopIfViolations(() -> Optional.of(violation)));

        assertSame(violation, exception.getViolations().get(0));
    }

    @Test
    @DisplayName("WHEN no violations THEN expect no exception")
    void validateAllAndStopIfViolations2() {
        validateAllAndStopIfViolations(Optional::empty);
    }

    @Test
    @DisplayName("WHEN all rules fail THEN expect only the first violation")
    void validateFindFirst1() {
        ViolationProvider[] validationRules = {
                ValidationRule.notNull("notNullCheck", null),
                ValidationRule.notBlank("notBlankCheck", ""),
        };

        List<Violation> violations = ValidationEngine.validateFindFirst(validationRules);

        assertEquals(1, violations.size());
        assertEquals("notNullCheck", violations.get(0).getFieldPath());
    }

    @Test
    @DisplayName("WHEN all rules pass THEN expect no violations")
    void validateFindFirst2() {
        ViolationProvider[] validationRules = {
                ValidationRule.notNull("notNullCheck", new Object()),
                ValidationRule.notBlank("notBlankCheck", "s"),
        };

        List<Violation> violations = ValidationEngine.validateFindFirst(validationRules);

        assertEquals(0, violations.size());
    }

    @Test
    @DisplayName("WHEN first rule pass, second rule fail, third rule fail THEN expect only the second violation")
    void validateFindFirst3() {
        ViolationProvider[] validationRules = {
                ValidationRule.notNull("notNullCheck", new Object()),
                ValidationRule.notBlank("notBlankCheck", ""),
                ValidationRule.notNull("notBlankCheck", null),
        };

        List<Violation> violations = ValidationEngine.validateFindFirst(validationRules);

        assertEquals(1, violations.size());
        assertEquals("notBlankCheck", violations.get(0).getFieldPath());
    }

    @Test
    @DisplayName("WHEN first rule fails THEN throw exception with the violation ")
    void validateFindFirstAndStopIfViolation1() {
        ViolationProvider[] validationRules = {
                ValidationRule.notNull("notNullCheck", null),
                ValidationRule.notBlank("notBlankCheck", ""),
        };

        ValidationException exception = assertThrows(ValidationException.class,
                () -> ValidationEngine.validateFindFirstAndStopIfViolation(validationRules));

        assertEquals(1, exception.getViolations().size());
        assertEquals("notNullCheck", exception.getViolations().get(0).getFieldPath());
    }

    @Test
    @DisplayName("WHEN no validations fail THEN expect no validation ")
    void validateFindFirstAndStopIfViolation2() {
        ViolationProvider[] validationRules = {
                ValidationRule.notNull("notNullCheck", new Object()),
        };

        ValidationEngine.validateFindFirstAndStopIfViolation(validationRules);
    }
}