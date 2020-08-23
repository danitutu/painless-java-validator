package com.vdt.painlessjavavalidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;

public class ValidationRule {
    /**
     * Checks if the value is not null.
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @return violation or success
     */
    public static ViolationProvider notNull(String fieldPath, Object value) {
        return () -> notNullRule(fieldPath, value);
    }

    /**
     * See {@link ValidationRule#notNull(java.lang.String, java.lang.Object)}
     */
    public static Optional<Violation> notNullRule(String fieldPath, Object value) {
        if (value == null) {
            return Optional.of(Violation.of(fieldPath, "validation.error.value.is.required", "The value is required."));
        }
        return Optional.empty();
    }

    /**
     * Checks if the value is not blank.
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @return violation or success
     */
    public static ViolationProvider notBlank(String fieldPath, CharSequence value) {
        return () -> notBlankRule(fieldPath, value);
    }

    /**
     * See {@link ValidationRule#notBlank(java.lang.String, java.lang.CharSequence)}
     */
    public static Optional<Violation> notBlankRule(String fieldPath, CharSequence value) {
        if (isBlank(value)) {
            return Optional.of(Violation.of(fieldPath, "validation.error.value.is.required", "The value is required."));
        }
        return Optional.empty();
    }

    /**
     * Checks if the value has the length between or equals to one of two limits.
     * The limits can be equal. The value will be checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param min       inferior limit
     * @param max       superior limit
     * @return violation or success
     * @throws IllegalArgumentException if min is greater than max.
     */
    public static ViolationProvider lengthBetween(String fieldPath, CharSequence value, int min, int max) {
        return () -> lengthBetweenRule(fieldPath, value, min, max);
    }

    /**
     * See {@link ValidationRule#lengthBetween(java.lang.String, java.lang.CharSequence, int, int)}
     */
    public static Optional<Violation> lengthBetweenRule(String fieldPath, CharSequence value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min is greater than max");
        }
        Optional<Violation> isNotNull = notNullRule(fieldPath, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
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

    /**
     * Checks if the value matches the provided regex. The value will be checked
     * against {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param regex     a valid regex pattern
     * @return violation or success
     */
    public static ViolationProvider matchRegex(String fieldPath, String value, String regex) {
        return () -> matchRegexRule(fieldPath, value, regex);
    }

    /**
     * See {@link ValidationRule#matchRegex(java.lang.String, java.lang.String, java.lang.String)}
     */
    public static Optional<Violation> matchRegexRule(String fieldPath, String value, String regex) {
        Optional<Violation> isNotNull = notNullRule(fieldPath, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
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

    /**
     * Checks if the value is between or equals to one of the two limits.
     * The limits can be equal. The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param min       inferior limit
     * @param max       superior limit
     * @return violation or success
     * @throws IllegalArgumentException if min is greater than max.
     */
    public static ViolationProvider inRange(String fieldPath, Integer value, int min, int max) {
        return () -> inRangeRule(fieldPath, value, min, max);
    }

    /**
     * See {@link ValidationRule#inRange(java.lang.String, java.lang.Integer, int, int)}
     */
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

    /**
     * Checks if the value is grater than or equals to the provided inferior limit.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param min       inferior limit
     * @return violation or success
     */
    public static ViolationProvider min(String fieldPath, Integer value, int min) {
        return () -> minRule(fieldPath, value, min);
    }

    /**
     * See {@link ValidationRule#min(java.lang.String, java.lang.Integer, int)}
     */
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

    /**
     * Checks if the value is smaller than or equals to the provided superior limit.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param max       superior limit
     * @return violation or success
     */
    public static ViolationProvider max(String fieldPath, Integer value, int max) {
        return () -> maxRule(fieldPath, value, max);
    }

    /**
     * See {@link ValidationRule#max(java.lang.String, java.lang.Integer, int)}
     */
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

    /**
     * Checks if the compared value is strictly after the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param other     other value
     * @param <T>       compared object type
     * @return violation or success
     */
    public static <T> ViolationProvider isAfter(String fieldPath,
                                                Comparable<T> value,
                                                T other) {
        return () -> isAfterRule(fieldPath, value, other);
    }

    /**
     * See {@link ValidationRule#isAfter(java.lang.String, java.lang.Comparable, java.lang.Object)}
     */
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

    /**
     * Checks if the compared value is after or equals to the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param other     other value
     * @param <T>       compared object type
     * @return violation or success
     */
    public static <T> ViolationProvider isAfterOrEqualsTo(String fieldPath,
                                                          Comparable<T> value,
                                                          T other) {
        return () -> isAfterOrEqualsToRule(fieldPath, value, other);
    }

    /**
     * See {@link ValidationRule#isAfterOrEqualsTo(java.lang.String, java.lang.Comparable, java.lang.Object)}
     */
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

    /**
     * Checks if the compared value is strictly before the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param other     other value
     * @param <T>       compared object type
     * @return violation or success
     */
    public static <T> ViolationProvider isBefore(String fieldPath,
                                                 Comparable<T> value,
                                                 T other) {
        return () -> isBeforeRule(fieldPath, value, other);
    }

    /**
     * See {@link ValidationRule#isBefore(java.lang.String, java.lang.Comparable, java.lang.Object)}
     */
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

    /**
     * Checks if the compared value is before or equals to the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param other     other value
     * @param <T>       compared object type
     * @return violation or success
     */
    public static <T> ViolationProvider isBeforeOrEqualsTo(String fieldPath,
                                                           Comparable<T> value,
                                                           T other) {
        return () -> isBeforeOrEqualsToRule(fieldPath, value, other);
    }

    /**
     * See {@link ValidationRule#isBeforeOrEqualsTo(java.lang.String, java.lang.Comparable, java.lang.Object)}
     */
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

    /**
     * Checks if the compared value equals the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param other     other value
     * @param <T>       compared object type
     * @return violation or success
     */
    public static <T> ViolationProvider equalsTo(String fieldPath,
                                                 Comparable<T> value,
                                                 T other) {
        return () -> equalsToRule(fieldPath, value, other);
    }

    /**
     * See {@link ValidationRule#equalsTo(java.lang.String, java.lang.Comparable, java.lang.Object)}
     */
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

    /**
     * Checks if the compared value equals the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param other     other value
     * @return violation or success
     */
    public static ViolationProvider equalsTo(String fieldPath, String value, String other) {
        return () -> equalsToRule(fieldPath, value, other);
    }

    /**
     * See {@link ValidationRule#equalsTo(java.lang.String, java.lang.String, java.lang.String)}
     */
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

    /**
     * Checks if the compared value is not equal to the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param fieldPath path to field
     * @param value     value to be checked
     * @param other     other value
     * @return violation or success
     */
    public static ViolationProvider notEqualsTo(String fieldPath, String value, String other) {
        return () -> notEqualsToRule(fieldPath, value, other);
    }

    /**
     * See {@link ValidationRule#notEqualsTo(java.lang.String, java.lang.String, java.lang.String)}
     */
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

    /**
     * Compares the two string against a provided rule. If the validation rule
     * is evaluated to true then the provided violation will be returned.
     *
     * @param fieldPath     path to field
     * @param value         value to be checked
     * @param other         other value
     * @param compareFunc   comparison function
     * @param violationFunc violation to be retrieved when compareFunc evaluates
     *                      to true
     * @return violation or success
     */
    public static ViolationProvider compareStrings(
            String fieldPath,
            String value,
            String other,
            BiFunction<String, String, Boolean> compareFunc,
            Supplier<Violation> violationFunc) {
        return () -> compareStringsRule(fieldPath, value, other, compareFunc, violationFunc);
    }

    /**
     * Returns violation in case compare function is true. For the first value
     * a notNull validation will also be applied.
     *
     * @param fieldPath path to field
     * @param value first value
     * @param other second value
     * @param compareFunc function that compares the two values
     * @param violationFunc the Violation that will be returned
     * @return violation or success
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
     * Returns violation in case compare function is true. For the first value
     * a notNull validation will also be applied.
     *
     * @param fieldPath path to field
     * @param value first value
     * @param other second value
     * @param compareFunc function that compares the two values
     * @param violationFunc the Violation that will be returned
     * @return violation or success
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
