package com.github.danitutu.painlessjavavalidator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;

public class ValidationRule {

    private static final String VALIDATION_ERROR_VALUE_IS_REQUIRED_MESSAGE = "validation.error.value.is.required";
    private static final String VALIDATION_ERROR_VALUE_IS_REQUIRED_DETAILS = "The value is required.";
    private static final String PARAM_NAME_OTHER = "other";

    private ValidationRule() {
    }

    /**
     * Checks if the value is null.
     *
     * @param field path to field
     * @param value value to be checked
     * @return violation or success
     */
    public static ViolationProvider isNull(String field, Object value) {
        return () -> isNullRule(field, value);
    }

    /**
     * See {@link ValidationRule#isNull(java.lang.String, java.lang.Object)}
     */
    public static Optional<Violation> isNullRule(String field, Object value) {
        return isTrueRule(() -> value == null,
                Violation.of(field, "validation.error.value.is.not.null", "The value is not null."));
    }

    /**
     * See {@link ValidationRule#isTrue(java.util.function.BooleanSupplier, com.github.danitutu.painlessjavavalidator.Violation)}
     */
    public static Optional<Violation> isTrueRule(BooleanSupplier condition, Violation violation) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        if (violation == null) {
            throw new IllegalArgumentException("Violation provider cannot be null");
        }
        return condition.getAsBoolean() ? Optional.empty() : Optional.of(violation);
    }

    /**
     * Checks if the value is not null.
     *
     * @param field path to field
     * @param value value to be checked
     * @return violation or success
     */
    public static ViolationProvider notNull(String field, Object value) {
        return () -> notNullRule(field, value);
    }

    /**
     * See {@link ValidationRule#notNull(java.lang.String, java.lang.Object)}
     */
    public static Optional<Violation> notNullRule(String field, Object value) {
        return isTrueRule(() -> value != null, Violation.of(
                field,
                VALIDATION_ERROR_VALUE_IS_REQUIRED_MESSAGE,
                VALIDATION_ERROR_VALUE_IS_REQUIRED_DETAILS));
    }

    /**
     * Checks if the value is empty.
     *
     * @param field path to field
     * @param value value to be checked
     * @return violation or success
     */
    public static ViolationProvider empty(String field, CharSequence value) {
        return () -> emptyRule(field, value);
    }

    /**
     * Checks if the condition is evaluated to true. If not then the violation
     * will be returned.
     *
     * @param condition condition
     * @param violation violation
     * @return violation or success
     * @throws IllegalArgumentException in case condition or violation is null
     */
    public static ViolationProvider isTrue(BooleanSupplier condition, Violation violation) {
        return () -> isTrueRule(condition, violation);
    }

    /**
     * Checks if the condition is evaluated to false. If not then the violation
     * will be returned.
     *
     * @param condition condition
     * @param violation violation
     * @return violation or success
     * @throws IllegalArgumentException in case condition or violation is null
     */
    public static ViolationProvider isFalse(BooleanSupplier condition, Violation violation) {
        return () -> isFalseRule(condition, violation);
    }

    /**
     * See {@link ValidationRule#empty(java.lang.String, java.lang.CharSequence)}
     */
    public static Optional<Violation> emptyRule(String field, CharSequence value) {
        return isTrueRule(() -> empty(value), Violation.of(field,
                VALIDATION_ERROR_VALUE_IS_REQUIRED_MESSAGE,
                VALIDATION_ERROR_VALUE_IS_REQUIRED_DETAILS));
    }

    private static boolean empty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * Checks if the value is not empty.
     *
     * @param field path to field
     * @param value value to be checked
     * @return violation or success
     */
    public static ViolationProvider notEmpty(String field, CharSequence value) {
        return () -> notEmptyRule(field, value);
    }

    /**
     * See {@link ValidationRule#notEmpty(java.lang.String, java.lang.CharSequence)}
     */
    public static Optional<Violation> notEmptyRule(String field, CharSequence value) {
        return isFalseRule(() -> empty(value),
                Violation.of(field, "validation.error.value.is.not.empty", "The value is not empty."));
    }

    /**
     * See {@link ValidationRule#isFalse(java.util.function.BooleanSupplier, com.github.danitutu.painlessjavavalidator.Violation)}
     */
    public static Optional<Violation> isFalseRule(BooleanSupplier condition, Violation violation) {
        if (condition == null) {
            throw new IllegalArgumentException("Condition cannot be null");
        }
        return isTrueRule(() -> !condition.getAsBoolean(), violation);
    }

    /**
     * Checks if the value is blank.
     *
     * @param field path to field
     * @param value value to be checked
     * @return violation or success
     */
    public static ViolationProvider blank(String field, CharSequence value) {
        return () -> blankRule(field, value);
    }

    /**
     * See {@link ValidationRule#blank(java.lang.String, java.lang.CharSequence)}
     */
    public static Optional<Violation> blankRule(String field, CharSequence value) {
        return isTrueRule(() -> isBlank(value),
                Violation.of(field, "validation.error.value.is.not.blank", "The value is empty."));
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

    /**
     * Checks if the value is not blank.
     *
     * @param field path to field
     * @param value value to be checked
     * @return violation or success
     */
    public static ViolationProvider notBlank(String field, CharSequence value) {
        return () -> notBlankRule(field, value);
    }

    /**
     * See {@link ValidationRule#notBlank(java.lang.String, java.lang.CharSequence)}
     */
    public static Optional<Violation> notBlankRule(String field, CharSequence value) {
        return isFalseRule(() -> isBlank(value), Violation.of(field,
                VALIDATION_ERROR_VALUE_IS_REQUIRED_MESSAGE,
                VALIDATION_ERROR_VALUE_IS_REQUIRED_DETAILS));
    }

    /**
     * Checks if the value has the length between or equals to one of two limits.
     * The limits can be equal. The value will be checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}
     *
     * @param field path to field
     * @param value value to be checked
     * @param min   inferior limit
     * @param max   superior limit
     * @return violation or success
     * @throws IllegalArgumentException if min is greater than max.
     */
    public static ViolationProvider lengthBetween(String field, CharSequence value, int min, int max) {
        return () -> lengthBetweenRule(field, value, min, max);
    }

    /**
     * See {@link ValidationRule#lengthBetween(java.lang.String, java.lang.CharSequence, int, int)}
     */
    public static Optional<Violation> lengthBetweenRule(String field, CharSequence value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min is greater than max");
        }
        Optional<Violation> isNotNull = notNullRule(field, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        return isTrueRule(() -> value.length() >= min && value.length() <= max, () -> {
            Map<String, Object> map = new HashMap<>();
            map.put("min", min);
            map.put("max", max);
            return Violation.of(
                    field,
                    "validation.error.string.value.not.between",
                    "Value is not in range.",
                    map
            );
        });
    }

    /**
     * See {@link #isTrue(BooleanSupplier, Violation)}
     *
     * @param violation violation provider
     */
    public static Optional<Violation> isTrueRule(BooleanSupplier condition, Supplier<Violation> violation) {
        return isTrueRule(condition, violation.get());
    }

    /**
     * Checks if the value matches the provided regex. The value will be checked
     * against {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}
     *
     * @param field path to field
     * @param value value to be checked
     * @param regex a valid regex pattern
     * @return violation or success
     */
    public static ViolationProvider matchRegex(String field, String value, String regex) {
        return () -> matchRegexRule(field, value, regex);
    }

    /**
     * See {@link ValidationRule#matchRegex(java.lang.String, java.lang.String, java.lang.String)}
     */
    public static Optional<Violation> matchRegexRule(String field, String value, String regex) {
        Optional<Violation> isNotNull = notNullRule(field, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        return isTrueRule(() -> value.matches(regex), Violation.of(
                field,
                "validation.error.string.value.regex.no.match",
                "Value does not match the expected regex.",
                singletonMap("regexPattern", regex)
        ));
    }

    /**
     * Checks if the value is between or equals to one of the two limits.
     * The limits can be equal. The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}
     *
     * @param field path to field
     * @param value value to be checked
     * @param min   inferior limit
     * @param max   superior limit
     * @return violation or success
     * @throws IllegalArgumentException if min is greater than max.
     */
    public static ViolationProvider inRange(String field, Integer value, int min, int max) {
        return () -> inRangeRule(field, value, min, max);
    }

    /**
     * See {@link ValidationRule#inRange(java.lang.String, java.lang.Integer, int, int)}
     */
    public static Optional<Violation> inRangeRule(String field, Integer value, int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min is greater than max");
        }
        Optional<Violation> isNotNull = notNullRule(field, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        return isTrueRule(() -> value >= min && value <= max, () -> {
            Map<String, Object> map = new HashMap<>();
            map.put("min", min);
            map.put("max", max);
            return Violation.of(
                    field,
                    "validation.error.integer.value.not.in.range",
                    "Value is not in range.",
                    map
            );
        });
    }

    /**
     * Checks if the value is grater than or equals to the provided inferior limit.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}
     *
     * @param field path to field
     * @param value value to be checked
     * @param min   inferior limit
     * @return violation or success
     */
    public static ViolationProvider min(String field, Integer value, int min) {
        return () -> minRule(field, value, min);
    }

    /**
     * See {@link ValidationRule#min(java.lang.String, java.lang.Integer, int)}
     */
    public static Optional<Violation> minRule(String field, Integer value, int min) {
        Optional<Violation> isNotNull = notNullRule(field, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        return isTrueRule(() -> value >= min, Violation.of(
                field,
                "validation.error.integer.value.smaller.than.min",
                "Value is smaller than min.",
                singletonMap("min", min)
        ));
    }

    /**
     * Checks if the value is smaller than or equals to the provided superior limit.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}
     *
     * @param field path to field
     * @param value value to be checked
     * @param max   superior limit
     * @return violation or success
     */
    public static ViolationProvider max(String field, Integer value, int max) {
        return () -> maxRule(field, value, max);
    }

    /**
     * See {@link ValidationRule#max(java.lang.String, java.lang.Integer, int)}
     */
    public static Optional<Violation> maxRule(String field, Integer value, int max) {
        Optional<Violation> isNotNull = notNullRule(field, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        return isTrueRule(() -> value <= max, Violation.of(
                field,
                "validation.error.integer.value.greater.than.max",
                "Value is greater than max.",
                singletonMap("max", max)
        ));
    }

    /**
     * Checks if the compared value is strictly after the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param field path to field
     * @param value value to be checked
     * @param other other value
     * @param <T>   compared object type
     * @return violation or success
     */
    public static <T> ViolationProvider after(String field,
                                              Comparable<T> value,
                                              T other) {
        return () -> afterRule(field, value, other);
    }

    /**
     * Returns violation in case compare function is true. For the first value
     * a notNull validation will also be applied.
     *
     * @param field         path to field
     * @param value         first value
     * @param other         second value
     * @param compareFunc   function that compares the two values
     * @param violationFunc the Violation that will be returned
     * @return violation or success
     */
    public static <T> Optional<Violation> compareComparableRule(
            String field, Comparable<T> value,
            T other,
            BiPredicate<Comparable<T>, T> compareFunc,
            Supplier<Violation> violationFunc) {
        Optional<Violation> valueIsNotNull = notNullRule(field, value);
        if (valueIsNotNull.isPresent()) {
            return valueIsNotNull;
        }
        return isFalseRule(() -> other == null || compareFunc.test(value, other), violationFunc);
    }

    /**
     * See {@link ValidationRule#after(java.lang.String, java.lang.Comparable, java.lang.Object)}
     */
    public static <T> Optional<Violation> afterRule(String field,
                                                    Comparable<T> value,
                                                    T other) {
        return compareComparableRule(
                field, value,
                other,
                (tComparable, t) -> value.compareTo(other) <= 0,
                () -> Violation.of(
                        field,
                        "validation.error.value.is.before.or.equal",
                        "The value is before or equal the other value.",
                        singletonMap(PARAM_NAME_OTHER, other == null ? null : other.toString())
                ));
    }

    /**
     * See {@link ValidationRule#isFalse(java.util.function.BooleanSupplier, com.github.danitutu.painlessjavavalidator.Violation)}
     *
     * @param violation violation provider
     */
    public static Optional<Violation> isFalseRule(BooleanSupplier condition, Supplier<Violation> violation) {
        return isTrueRule(() -> !condition.getAsBoolean(), violation);
    }

    /**
     * Checks if the compared value is after or equals to the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param field path to field
     * @param value value to be checked
     * @param other other value
     * @param <T>   compared object type
     * @return violation or success
     */
    public static <T> ViolationProvider afterOrEqualsTo(String field,
                                                        Comparable<T> value,
                                                        T other) {
        return () -> afterOrEqualsToRule(field, value, other);
    }

    /**
     * See {@link ValidationRule#afterOrEqualsTo(java.lang.String, java.lang.Comparable, java.lang.Object)}
     */
    public static <T> Optional<Violation> afterOrEqualsToRule(String field,
                                                              Comparable<T> value,
                                                              T other) {
        return compareComparableRule(
                field, value,
                other,
                (tComparable, t) -> value.compareTo(other) < 0,
                () -> Violation.of(
                        field,
                        "validation.error.value.is.before",
                        "The value is before the other value.",
                        singletonMap(PARAM_NAME_OTHER, other == null ? null : other.toString())
                ));
    }

    /**
     * Checks if the compared value is strictly before the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param field path to field
     * @param value value to be checked
     * @param other other value
     * @param <T>   compared object type
     * @return violation or success
     */
    public static <T> ViolationProvider before(String field,
                                               Comparable<T> value,
                                               T other) {
        return () -> beforeRule(field, value, other);
    }

    /**
     * See {@link ValidationRule#before(java.lang.String, java.lang.Comparable, java.lang.Object)}
     */
    public static <T> Optional<Violation> beforeRule(String field,
                                                     Comparable<T> value,
                                                     T other) {
        return compareComparableRule(
                field, value,
                other,
                (tComparable, t) -> value.compareTo(other) >= 0,
                () -> Violation.of(
                        field,
                        "validation.error.value.is.after.or.equal",
                        "The value is after or equal the other value.",
                        singletonMap(PARAM_NAME_OTHER, other == null ? null : other.toString())
                ));
    }

    /**
     * Checks if the compared value is before or equals to the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param field path to field
     * @param value value to be checked
     * @param other other value
     * @param <T>   compared object type
     * @return violation or success
     */
    public static <T> ViolationProvider beforeOrEqualsTo(String field,
                                                         Comparable<T> value,
                                                         T other) {
        return () -> beforeOrEqualsToRule(field, value, other);
    }

    /**
     * See {@link ValidationRule#beforeOrEqualsTo(java.lang.String, java.lang.Comparable, java.lang.Object)}
     */
    public static <T> Optional<Violation> beforeOrEqualsToRule(String field,
                                                               Comparable<T> value,
                                                               T other) {
        return compareComparableRule(
                field, value,
                other,
                (tComparable, t) -> value.compareTo(other) > 0,
                () -> Violation.of(
                        field,
                        "validation.error.value.is.after",
                        "The value is after the other value.",
                        singletonMap(PARAM_NAME_OTHER, other == null ? null : other.toString())
                ));
    }

    /**
     * Checks if the compared value equals the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param field path to field
     * @param value value to be checked
     * @param other other value
     * @param <T>   compared object type
     * @return violation or success
     */
    public static <T> ViolationProvider equalsTo(String field,
                                                 Comparable<T> value,
                                                 T other) {
        return () -> equalsToRule(field, value, other);
    }

    /**
     * See {@link ValidationRule#equalsTo(java.lang.String, java.lang.Comparable, java.lang.Object)}
     */
    public static <T> Optional<Violation> equalsToRule(String field,
                                                       Comparable<T> value,
                                                       T other) {
        return compareComparableRule(
                field, value,
                other,
                (tComparable, t) -> value.compareTo(other) != 0,
                () -> Violation.of(
                        field,
                        "validation.error.value.is.not.equal",
                        "The value is not equal to the other value.",
                        singletonMap(PARAM_NAME_OTHER, other == null ? null : other.toString())
                ));
    }

    /**
     * Checks if the compared value equals the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param field path to field
     * @param value value to be checked
     * @param other other value
     * @return violation or success
     */
    public static ViolationProvider equalsTo(String field, String value, String other) {
        return () -> equalsToRule(field, value, other);
    }

    /**
     * See {@link ValidationRule#equalsTo(java.lang.String, java.lang.String, java.lang.String)}
     */
    public static Optional<Violation> equalsToRule(String field, String value, String other) {
        return compareStringsRule(
                field, value,
                other,
                (s, s2) -> !value.equals(other),
                () -> Violation.of(
                        field,
                        "validation.error.string.is.not.equal",
                        "The value is not equal to the other value.",
                        singletonMap(PARAM_NAME_OTHER, other)
                ));
    }

    /**
     * Compares the two string against a provided rule. If the validation rule
     * is evaluated to true then the provided violation will be returned.
     *
     * @param field         path to field
     * @param value         value to be checked
     * @param other         other value
     * @param compareFunc   comparison function
     * @param violationFunc violation to be retrieved when compareFunc evaluates
     *                      to true
     * @return violation or success
     */
    public static ViolationProvider compareStrings(
            String field,
            String value,
            String other,
            BiPredicate<String, String> compareFunc,
            Supplier<Violation> violationFunc) {
        return () -> compareStringsRule(field, value, other, compareFunc, violationFunc);
    }

    /**
     * Checks if the compared value is not equal to the other.
     * The value will be also checked against
     * {@link ValidationRule#notNullRule(java.lang.String, java.lang.Object)}.
     * If other is null then a violation will be returned.
     *
     * @param field path to field
     * @param value value to be checked
     * @param other other value
     * @return violation or success
     */
    public static ViolationProvider notEqualsTo(String field, String value, String other) {
        return () -> notEqualsToRule(field, value, other);
    }

    /**
     * See {@link ValidationRule#notEqualsTo(java.lang.String, java.lang.String, java.lang.String)}
     */
    public static Optional<Violation> notEqualsToRule(String field, String value, String other) {
        return compareStringsRule(
                field, value,
                other,
                (s, s2) -> value.equals(other),
                () -> Violation.of(
                        field,
                        "validation.error.string.is.equal",
                        "The value is equal to the other value."
                ));
    }

    /**
     * Returns violation in case compare function is true. For the first value
     * a notNull validation will also be applied.
     *
     * @param field         path to field
     * @param value         first value
     * @param other         second value
     * @param compareFunc   function that compares the two values
     * @param violationFunc the Violation that will be returned
     * @return violation or success
     */
    public static Optional<Violation> compareStringsRule(
            String field, String value,
            String other,
            BiPredicate<String, String> compareFunc,
            Supplier<Violation> violationFunc) {
        Optional<Violation> isNotNull = notNullRule(field, value);
        if (isNotNull.isPresent()) {
            return isNotNull;
        }
        return isFalseRule(() -> compareFunc.test(value, other), violationFunc);
    }

    public static <T> ViolationProvider compareComparable(
            String field, Comparable<T> value,
            T other,
            BiPredicate<Comparable<T>, T> compareFunc,
            Supplier<Violation> violationFunc) {
        return () -> compareComparableRule(field, value, other, compareFunc, violationFunc);
    }
}
