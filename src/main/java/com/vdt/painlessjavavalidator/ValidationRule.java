package com.vdt.painlessjavavalidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;

public class ValidationRule {
    public static ViolationProvider notNull(String fieldPath, Object value) {
        return () -> notNullRule(fieldPath, value);
    }

    public static Optional<Violation> notNullRule(String fieldPath, Object value) {
        if (value == null) {
            return Optional.of(Violation.of(fieldPath, "validation.error.value.is.required", "The value is required."));
        }
        return Optional.empty();
    }

    public static ViolationProvider notBlank(String fieldPath, CharSequence value) {
        return () -> notBlankRule(fieldPath, value);
    }

    public static Optional<Violation> notBlankRule(String fieldPath, CharSequence value) {
        if (isBlank(value)) {
            return Optional.of(Violation.of(fieldPath, "validation.error.value.is.required", "The value is required."));
        }
        return Optional.empty();
    }

    public static ViolationProvider lengthBetween(String fieldPath, CharSequence value, int min, int max) {
        return () -> lengthBetweenRule(fieldPath, value, min, max);
    }

    public static Optional<Violation> lengthBetweenRule(String fieldPath, CharSequence value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min is greater than max");
        }
        Optional<Violation> isNotBlank = notBlankRule(fieldPath, value);
        if (isNotBlank.isPresent()) {
            return isNotBlank;
        }
        if (value.length() < min || value.length() > max) {
            Map<String, Object> map = new HashMap<>();
            map.put("min", min);
            map.put("max", max);
            return Optional.of(Violation.of(
                    fieldPath,
                    "validation.error.string.value.not.between",
                    "Value is not in range.",
                    map
            ));
        }
        return Optional.empty();
    }

    public static ViolationProvider matchRegex(String fieldPath, String value, String regex) {
        return () -> matchRegexRule(fieldPath, value, regex);
    }

    public static Optional<Violation> matchRegexRule(String fieldPath, String value, String regex) {
        Optional<Violation> isNotBlank = notBlankRule(fieldPath, value);
        if (isNotBlank.isPresent()) {
            return isNotBlank;
        }
        if (!value.matches(regex)) {
            return Optional.of(Violation.of(
                    fieldPath,
                    "validation.error.string.value.regex.no.match",
                    "Value does not match the expected regex.",
                    singletonMap("regexPattern", regex)
            ));
        }
        return Optional.empty();
    }

    public static ViolationProvider inRange(String fieldPath, Integer value, int min, int max) {
        return () -> inRangeRule(fieldPath, value, min, max);
    }

    public static Optional<Violation> inRangeRule(String fieldPath, Integer value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min is greater than max");
        }
        Optional<Violation> isNotNull = notNullRule(fieldPath, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        if (value < min || value > max) {
            Map<String, Object> map = new HashMap<>();
            map.put("min", min);
            map.put("max", max);
            return Optional.of(Violation.of(
                    fieldPath,
                    "validation.error.integer.value.not.in.range",
                    "Value is not in range.",
                    map
            ));
        }
        return Optional.empty();
    }

    public static ViolationProvider min(String fieldPath, Integer value, int min) {
        return () -> minRule(fieldPath, value, min);
    }

    public static Optional<Violation> minRule(String fieldPath, Integer value, int min) {
        Optional<Violation> isNotNull = notNullRule(fieldPath, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        if (value < min) {
            return Optional.of(Violation.of(
                    fieldPath,
                    "validation.error.integer.value.smaller.than.min",
                    "Value is smaller than min.",
                    singletonMap("min", min)
            ));
        }
        return Optional.empty();
    }

    public static ViolationProvider max(String fieldPath, Integer value, int max) {
        return () -> maxRule(fieldPath, value, max);
    }

    public static Optional<Violation> maxRule(String fieldPath, Integer value, int max) {
        Optional<Violation> isNotNull = notNullRule(fieldPath, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        if (value > max) {
            return Optional.of(Violation.of(
                    fieldPath,
                    "validation.error.integer.value.greater.than.max",
                    "Value is greater than max.",
                    singletonMap("max", max)
            ));
        }
        return Optional.empty();
    }

    public static <T> ViolationProvider isAfter(String fieldPath,
                                                Comparable<T> value,
                                                T other) {
        return () -> isAfterRule(fieldPath, value, other);
    }

    public static <T> Optional<Violation> isAfterRule(String fieldPath,
                                                      Comparable<T> value,
                                                      T other) {
        return compareComparablesRule(
                fieldPath, value,
                other,
                (tComparable, t) -> value.compareTo(other) <= 0,
                () -> Violation.of(
                        fieldPath,
                        "validation.error.value.is.before.or.equal",
                        "The value is before or equal the other value.",
                        singletonMap("other", other == null ? null : other.toString())
                ));
    }

    public static <T> ViolationProvider isAfterOrEqualsTo(String fieldPath,
                                                          Comparable<T> value,
                                                          T other) {
        return () -> isAfterOrEqualsToRule(fieldPath, value, other);
    }

    public static <T> Optional<Violation> isAfterOrEqualsToRule(String fieldPath,
                                                                Comparable<T> value,
                                                                T other) {
        return compareComparablesRule(
                fieldPath, value,
                other,
                (tComparable, t) -> value.compareTo(other) < 0,
                () -> Violation.of(
                        fieldPath,
                        "validation.error.value.is.before",
                        "The value is before the other value.",
                        singletonMap("other", other == null ? null : other.toString())
                ));
    }

    public static <T> ViolationProvider isBefore(String fieldPath,
                                                 Comparable<T> value,
                                                 T other) {
        return () -> isBeforeRule(fieldPath, value, other);
    }

    public static <T> Optional<Violation> isBeforeRule(String fieldPath,
                                                       Comparable<T> value,
                                                       T other) {
        return compareComparablesRule(
                fieldPath, value,
                other,
                (tComparable, t) -> value.compareTo(other) >= 0,
                () -> Violation.of(
                        fieldPath,
                        "validation.error.value.is.after.or.equal",
                        "The value is after or equal the other value.",
                        singletonMap("other", other == null ? null : other.toString())
                ));
    }

    public static <T> ViolationProvider isBeforeOrEqualsTo(String fieldPath,
                                                           Comparable<T> value,
                                                           T other) {
        return () -> isBeforeOrEqualsToRule(fieldPath, value, other);
    }

    public static <T> Optional<Violation> isBeforeOrEqualsToRule(String fieldPath,
                                                                 Comparable<T> value,
                                                                 T other) {
        return compareComparablesRule(
                fieldPath, value,
                other,
                (tComparable, t) -> value.compareTo(other) > 0,
                () -> Violation.of(
                        fieldPath,
                        "validation.error.value.is.after",
                        "The value is after the other value.",
                        singletonMap("other", other == null ? null : other.toString())
                ));
    }

    public static <T> ViolationProvider equalsTo(String fieldPath,
                                                 Comparable<T> value,
                                                 T other) {
        return () -> equalsToRule(fieldPath, value, other);
    }

    public static <T> Optional<Violation> equalsToRule(String fieldPath,
                                                       Comparable<T> value,
                                                       T other) {
        return compareComparablesRule(
                fieldPath, value,
                other,
                (tComparable, t) -> value.compareTo(other) != 0,
                () -> Violation.of(
                        fieldPath,
                        "validation.error.value.is.not.equal",
                        "The value is not equal to the other value.",
                        singletonMap("other", other == null ? null : other.toString())
                ));
    }

    public static ViolationProvider equalsTo(String fieldPath, String value, String other) {
        return () -> equalsToRule(fieldPath, value, other);
    }

    public static Optional<Violation> equalsToRule(String fieldPath, String value, String other) {
        return compareStringsRule(
                fieldPath, value,
                other,
                (s, s2) -> !value.equals(other),
                () -> Violation.of(
                        fieldPath,
                        "validation.error.string.is.not.equal",
                        "The value is not equal to the other value.",
                        singletonMap("other", other)
                ));
    }

    public static ViolationProvider notEqualsTo(String fieldPath, String value, String other) {
        return () -> notEqualsToRule(fieldPath, value, other);
    }

    public static Optional<Violation> notEqualsToRule(String fieldPath, String value, String other) {
        return compareStringsRule(
                fieldPath, value,
                other,
                (s, s2) -> value.equals(other),
                () -> Violation.of(
                        fieldPath,
                        "validation.error.string.is.equal",
                        "The value is equal to the other value."
                ));
    }

    public static ViolationProvider compareStrings(
            String fieldPath, String value,
            String other,
            BiFunction<String, String, Boolean> compareFunc,
            Supplier<Violation> violationFunc) {
        return () -> compareStringsRule(fieldPath, value, other, compareFunc, violationFunc);
    }

    /**
     * Return violation in case compare function is true
     *
     * @param fieldPath
     * @param value
     * @param other
     * @param compareFunc
     * @param violationFunc
     * @return
     */
    public static Optional<Violation> compareStringsRule(
            String fieldPath, String value,
            String other,
            BiFunction<String, String, Boolean> compareFunc,
            Supplier<Violation> violationFunc) {
        Optional<Violation> isNotNull = notNullRule(fieldPath, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        if (compareFunc.apply(value, other)) {
            return Optional.of(violationFunc.get());
        }
        return Optional.empty();
    }

    public static <T> ViolationProvider compareComparables(
            String fieldPath, Comparable<T> value,
            T other,
            BiFunction<Comparable<T>, T, Boolean> compareFunc,
            Supplier<Violation> violationFunc) {
        return () -> compareComparablesRule(fieldPath, value, other, compareFunc, violationFunc);
    }

    /**
     * Return violation in case compareFunc is true
     *
     * @param fieldPath
     * @param value
     * @param other
     * @param compareFunc
     * @param violationFunc
     * @param <T>
     * @return
     */
    public static <T> Optional<Violation> compareComparablesRule(
            String fieldPath, Comparable<T> value,
            T other,
            BiFunction<Comparable<T>, T, Boolean> compareFunc,
            Supplier<Violation> violationFunc) {
        Optional<Violation> valueIsNotNull = notNullRule(fieldPath, value);
        if (valueIsNotNull.isPresent()) {
            return valueIsNotNull;
        }
        if (other == null || compareFunc.apply(value, other)) {
            return Optional.of(violationFunc.get());
        }
        return Optional.empty();
    }

    private static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }
        }
        return true;
    }
}
