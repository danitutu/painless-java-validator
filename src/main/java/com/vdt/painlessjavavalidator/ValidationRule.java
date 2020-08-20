package com.vdt.painlessjavavalidator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;

public class ValidationRule {
    public static Optional<Violation> notNull(String fieldPath, Object value) {
        if (value == null) {
            return Optional.of(Violation.of(fieldPath, "validation.error.value.is.required", "The value is required."));
        }
        return Optional.empty();
    }

    public static Optional<Violation> notBlank(String fieldPath, CharSequence value) {
        if (isBlank(value)) {
            return Optional.of(Violation.of(fieldPath, "validation.error.value.is.required", "The value is required."));
        }
        return Optional.empty();
    }

    public static Optional<Violation> lengthBetween(String fieldPath, CharSequence value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min is greater than max");
        }
        Optional<Violation> isNotBlank = notBlank(fieldPath, value);
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

    public static Optional<Violation> matchRegex(String fieldPath, String value, String regex) {
        Optional<Violation> isNotBlank = notBlank(fieldPath, value);
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

    public static Optional<Violation> inRange(String fieldPath, Integer value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min is greater than max");
        }
        Optional<Violation> isNotNull = notNull(fieldPath, value);
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

    public static Optional<Violation> min(String fieldPath, Integer value, int min) {
        Optional<Violation> isNotNull = notNull(fieldPath, value);
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

    public static Optional<Violation> max(String fieldPath, Integer value, int max) {
        Optional<Violation> isNotNull = notNull(fieldPath, value);
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

    public static <T> Optional<Violation> isAfter(String fieldPath,
                                                  Comparable<T> value,
                                                  T other) {
        return compareComparables(
                fieldPath, value,
                other,
                (tComparable, t) -> value.compareTo(other) <= 0,
                () -> Violation.of(
                        fieldPath,
                        "validation.error.value.is.before.or.equal",
                        "The value is before or equal the other value.",
                        singletonMap("other", other.toString())
                ));
    }

    public static <T> Optional<Violation> isAfterOrEqualsTo(String fieldPath,
                                                            Comparable<T> value,
                                                            T other) {
        return compareComparables(
                fieldPath, value,
                other,
                (tComparable, t) -> value.compareTo(other) < 0,
                () -> Violation.of(
                        fieldPath,
                        "validation.error.value.is.before",
                        "The value is before the other value.",
                        singletonMap("other", other.toString())
                ));
    }

    public static <T> Optional<Violation> isBefore(String fieldPath,
                                                   Comparable<T> value,
                                                   T other) {
        return compareComparables(
                fieldPath, value,
                other,
                (tComparable, t) -> value.compareTo(other) >= 0,
                () -> Violation.of(
                        fieldPath,
                        "validation.error.value.is.after.or.equal",
                        "The value is after or equal the other value.",
                        singletonMap("other", other.toString())
                ));
    }

    public static <T> Optional<Violation> isBeforeOrEqualTo(String fieldPath,
                                                            Comparable<T> value,
                                                            T other) {
        return compareComparables(
                fieldPath, value,
                other,
                (tComparable, t) -> value.compareTo(other) > 0,
                () -> Violation.of(
                        fieldPath,
                        "validation.error.value.is.after",
                        "The value is after the other value.",
                        singletonMap("other", other.toString())
                ));
    }

    public static <T> Optional<Violation> equalsTo(String fieldPath,
                                                   Comparable<T> value,
                                                   T other) {
        return compareComparables(
                fieldPath, value,
                other,
                (tComparable, t) -> value.compareTo(other) == 0,
                () -> Violation.of(
                        fieldPath,
                        "validation.error.value.is.not.equal",
                        "The value is not equal to the other value.",
                        singletonMap("other", other.toString())
                ));
    }

    public static Optional<Violation> equalsTo(String fieldPath, String value, String other) {
        return compareStrings(
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

    public static Optional<Violation> notEqualsTo(String fieldPath, String value, String other) {
        return compareStrings(
                fieldPath, value,
                other,
                (s, s2) -> value.equals(other),
                () -> Violation.of(
                        fieldPath,
                        "validation.error.string.is.equal",
                        "The value is equal to the other value."
                ));
    }

    public static Optional<Violation> compareStrings(
            String fieldPath, String value,
            String other,
            BiFunction<String, String, Boolean> compareFunc,
            Supplier<Violation> violationFunc) {
        Optional<Violation> isNotNull = notNull(fieldPath, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        if(compareFunc.apply(value, other)) {
            return Optional.of(violationFunc.get());
        }
        return Optional.empty();
    }

    public static <T> Optional<Violation> compareComparables(
            String fieldPath, Comparable<T> value,
            T other,
            BiFunction<Comparable<T>, T, Boolean> compareFunc,
            Supplier<Violation> violationFunc) {
        Optional<Violation> isNotNull = notNull(fieldPath, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        if (compareFunc.apply(value, other)) {
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
