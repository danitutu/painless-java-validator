package com.github.danitutu.painlessjavavalidator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import static java.util.Collections.singletonMap;

public class ValidationRule {

  private static final String VALIDATION_ERROR_VALUE_IS_REQUIRED_MESSAGE =
          "validation.error.value.is.required";
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
   * See {@link #isNull(String, Object)}
   */
  public static Optional<Violation> isNullRule(String field, Object value) {
    return isTrueRule(
            () -> value == null,
            Violation.of(field, "validation.error.value.is.not.null", "The value is not null."));
  }

  /** See {@link #isTrue(BooleanSupplier, Violation)} */
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
   * See {@link #notNull(String, Object)}
   */
  public static Optional<Violation> notNullRule(String field, Object value) {
    return isTrueRule(
            () -> value != null,
            Violation.of(
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
   * See {@link #empty(String, CharSequence)}
   */
  public static Optional<Violation> emptyRule(String field, CharSequence value) {
    return isTrueRule(
            () -> empty(value),
            Violation.of(
                    field,
                    VALIDATION_ERROR_VALUE_IS_REQUIRED_MESSAGE,
                    VALIDATION_ERROR_VALUE_IS_REQUIRED_DETAILS));
  }

  private static boolean empty(final CharSequence cs) {
    return cs == null || cs.length() == 0;
  }

  /**
   * Checks if the condition is evaluated to true. If not then the violation will be returned.
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
   * Checks if the condition is evaluated to false. If not then the violation will be returned.
   *
   * @param condition condition
   * @param violation violation
   * @return violation or success
   * @throws IllegalArgumentException in case condition or violation is null
   */
  public static ViolationProvider isFalse(BooleanSupplier condition, Violation violation) {
    return () -> isFalseRule(condition, violation);
  }

  /** See {@link #isFalse(BooleanSupplier, Violation)} */
  public static Optional<Violation> isFalseRule(BooleanSupplier condition, Violation violation) {
    if (condition == null) {
      throw new IllegalArgumentException("Condition cannot be null");
    }
    return isTrueRule(() -> !condition.getAsBoolean(), violation);
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
   * See {@link #notEmpty(String, CharSequence)}
   */
  public static Optional<Violation> notEmptyRule(String field, CharSequence value) {
    return isFalseRule(
            () -> empty(value),
            Violation.of(field, "validation.error.value.is.not.empty", "The value is not empty."));
  }

  /**
   * Checks if the value is blank. Blank field means null, empty string or string with blanks.
   *
   * @param field path to field
   * @param value value to be checked
   * @return violation or success
   */
  public static ViolationProvider blank(String field, CharSequence value) {
    return () -> blankRule(field, value);
  }

  /**
   * See {@link #blank(String, CharSequence)}
   */
  public static Optional<Violation> blankRule(String field, CharSequence value) {
    return isTrueRule(
            () -> isBlank(value),
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
   * Checks if the value is not blank. Blank field means null, empty string or string with blanks.
   *
   * @param field path to field
   * @param value value to be checked
   * @return violation or success
   */
  public static ViolationProvider notBlank(String field, CharSequence value) {
    return () -> notBlankRule(field, value);
  }

  /**
   * See {@link #notBlank(String, CharSequence)}
   */
  public static Optional<Violation> notBlankRule(String field, CharSequence value) {
    return isFalseRule(
            () -> isBlank(value),
            Violation.of(
                    field,
                    VALIDATION_ERROR_VALUE_IS_REQUIRED_MESSAGE,
                    VALIDATION_ERROR_VALUE_IS_REQUIRED_DETAILS));
  }

  /**
   * Checks if the value has the length between or equals to one of two limits. The limits can be
   * equal. If the value is null then no violation is returned.
   *
   * @param field path to field
   * @param value value to be checked
   * @param min   inferior limit
   * @param max   superior limit
   * @return violation or success
   * @throws IllegalArgumentException if min is greater than max.
   */
  public static ViolationProvider lengthBetween(
          String field, CharSequence value, int min, int max) {
    return () -> lengthBetweenRule(field, value, min, max);
  }

  /**
   * See {@link #lengthBetween(String, CharSequence, int, int)}
   */
  public static Optional<Violation> lengthBetweenRule(
          String field, CharSequence value, int min, int max) {
    if (min > max) {
      throw new IllegalArgumentException("min is greater than max");
    }
    if (value == null) {
      return Optional.empty();
    }
    return isTrueRule(
            () -> value.length() >= min && value.length() <= max,
            () -> {
              Map<String, Object> map = new HashMap<>();
              map.put("min", min);
              map.put("max", max);
          return Violation.of(
              field, "validation.error.string.value.not.between", "Value is not in range.", map);
        });
  }

  /**
   * See {@link #isTrue(BooleanSupplier, Violation)}
   *
   * @param violation violation provider
   */
  public static Optional<Violation> isTrueRule(
          BooleanSupplier condition, Supplier<Violation> violation) {
    return isTrueRule(condition, violation.get());
  }

  /**
   * Checks if the value matches the provided regex. If the value is null then no violation is
   * returned.
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
   * See {@link #matchRegex(String, String, String)}
   */
  public static Optional<Violation> matchRegexRule(String field, String value, String regex) {
    if (regex == null) {
      throw new IllegalArgumentException("regex is required");
    }
    if (value == null) {
      return Optional.empty();
    }
    return isTrueRule(
            () -> value.matches(regex),
            Violation.of(
                    field,
                    "validation.error.string.value.regex.no.match",
                    "Value does not match the expected regex.",
                    singletonMap("regexPattern", regex)));
  }

  /**
   * Checks if the value is between or equals to one of the two limits. The limits can be equal. If
   * the value is null then no violation is returned.
   *
   * @param field path to field
   * @param value value to be checked
   * @param min inferior limit
   * @param max superior limit
   * @return violation or success
   * @throws IllegalArgumentException if min is greater than max.
   */
  public static ViolationProvider inRange(String field, Integer value, int min, int max) {
    return () -> inRangeRule(field, value, min, max);
  }

  /**
   * See {@link #inRange(String, Integer, int, int)}
   */
  public static Optional<Violation> inRangeRule(String field, Integer value, int min, int max) {
    if (min > max) {
      throw new IllegalArgumentException("min is greater than max");
    }
    if (value == null) {
      return Optional.empty();
    }
    return isTrueRule(
            () -> value >= min && value <= max,
            () -> {
              Map<String, Object> map = new HashMap<>();
              map.put("min", min);
              map.put("max", max);
              return Violation.of(
                      field, "validation.error.integer.value.not.in.range", "Value is not in range.", map);
            });
  }

  /**
   * Checks if the value is grater than or equals to the provided inferior limit. If the value is
   * null then no violation is returned.
   *
   * @param field path to field
   * @param value value to be checked
   * @param min inferior limit
   * @return violation or success
   */
  public static ViolationProvider min(String field, Integer value, int min) {
    return () -> minRule(field, value, min);
  }

  /**
   * See {@link #min(String, Integer, int)}
   */
  public static Optional<Violation> minRule(String field, Integer value, int min) {
    if (value == null) {
      return Optional.empty();
    }
    return isTrueRule(
            () -> value >= min,
            Violation.of(
                    field,
                    "validation.error.integer.value.smaller.than.min",
                    "Value is smaller than min.",
                    singletonMap("min", min)));
  }

  public static ViolationProvider positiveOrZero(String field, BigDecimal value) {
    return () -> positiveOrZeroRule(field, value);
  }

  public static Optional<Violation> positiveOrZeroRule(String field, BigDecimal value) {
    return positiveOrZeroRule(field, () -> value.compareTo(BigDecimal.ZERO) >= 0, value);
  }

  private static Optional<Violation> positiveOrZeroRule(
          String field, BooleanSupplier conditionProvider, Object value) {
    return commonPositiveAndNegative(
            conditionProvider,
            value,
            Violation.of(
                    field,
                    "validation.error.negative.value",
                    "The value must be a positive number (zero allowed)."));
  }

  private static Optional<Violation> commonPositiveAndNegative(
          BooleanSupplier conditionProvider, Object value, Violation violation) {
    if (value == null) {
      return Optional.empty();
    }
    return isTrueRule(conditionProvider, violation);
  }

  public static ViolationProvider positiveOrZero(String field, BigInteger value) {
    return () -> positiveOrZeroRule(field, value);
  }

  public static Optional<Violation> positiveOrZeroRule(String field, BigInteger value) {
    return positiveOrZeroRule(field, () -> value.compareTo(BigInteger.ZERO) >= 0, value);
  }

  public static ViolationProvider positiveOrZero(String field, Double value) {
    return () -> positiveOrZeroRule(field, value);
  }

  public static Optional<Violation> positiveOrZeroRule(String field, Double value) {
    return positiveOrZeroRule(field, () -> value >= 0, value);
  }

  public static ViolationProvider positiveOrZero(String field, Float value) {
    return () -> positiveOrZeroRule(field, value);
  }

  public static Optional<Violation> positiveOrZeroRule(String field, Float value) {
    return positiveOrZeroRule(field, () -> value >= 0, value);
  }

  public static ViolationProvider positiveOrZero(String field, Long value) {
    return () -> positiveOrZeroRule(field, value);
  }

  public static Optional<Violation> positiveOrZeroRule(String field, Long value) {
    return positiveOrZeroRule(field, () -> value >= 0, value);
  }

  public static ViolationProvider positiveOrZero(String field, Integer value) {
    return () -> positiveOrZeroRule(field, value);
  }

  public static Optional<Violation> positiveOrZeroRule(String field, Integer value) {
    return positiveOrZeroRule(field, () -> value >= 0, value);
  }

  public static ViolationProvider positive(String field, BigDecimal value) {
    return () -> positiveRule(field, value);
  }

  public static Optional<Violation> positiveRule(String field, BigDecimal value) {
    return positiveRule(field, () -> value.compareTo(BigDecimal.ZERO) > 0, value);
  }

  private static Optional<Violation> positiveRule(
          String field, BooleanSupplier conditionProvider, Object value) {
    return commonPositiveAndNegative(
            conditionProvider,
            value,
            Violation.of(
                    field,
                    "validation.error.negative.or.zero.value",
                    "The value must be a positive number (zero not allowed)."));
  }

  public static ViolationProvider positive(String field, BigInteger value) {
    return () -> positiveRule(field, value);
  }

  public static Optional<Violation> positiveRule(String field, BigInteger value) {
    return positiveRule(field, () -> value.compareTo(BigInteger.ZERO) > 0, value);
  }

  public static ViolationProvider positive(String field, Double value) {
    return () -> positiveRule(field, value);
  }

  public static Optional<Violation> positiveRule(String field, Double value) {
    return positiveRule(field, () -> value > 0, value);
  }

  public static ViolationProvider positive(String field, Float value) {
    return () -> positiveRule(field, value);
  }

  public static Optional<Violation> positiveRule(String field, Float value) {
    return positiveRule(field, () -> value > 0, value);
  }

  public static ViolationProvider positive(String field, Long value) {
    return () -> positiveRule(field, value);
  }

  public static Optional<Violation> positiveRule(String field, Long value) {
    return positiveRule(field, () -> value > 0, value);
  }

  public static ViolationProvider positive(String field, Integer value) {
    return () -> positiveRule(field, value);
  }

  public static Optional<Violation> positiveRule(String field, Integer value) {
    return positiveRule(field, () -> value > 0, value);
  }

  public static ViolationProvider negativeOrZero(String field, BigDecimal value) {
    return () -> negativeOrZeroRule(field, value);
  }

  public static Optional<Violation> negativeOrZeroRule(String field, BigDecimal value) {
    return negativeOrZeroRule(field, () -> value.compareTo(BigDecimal.ZERO) <= 0, value);
  }

  private static Optional<Violation> negativeOrZeroRule(
          String field, BooleanSupplier conditionProvider, Object value) {
    return commonPositiveAndNegative(
            conditionProvider,
            value,
            Violation.of(
                    field,
                    "validation.error.positive.value",
                    "The value must be a negative number (zero allowed."));
  }

  public static ViolationProvider negativeOrZero(String field, BigInteger value) {
    return () -> negativeOrZeroRule(field, value);
  }

  public static Optional<Violation> negativeOrZeroRule(String field, BigInteger value) {
    return negativeOrZeroRule(field, () -> value.compareTo(BigInteger.ZERO) <= 0, value);
  }

  public static ViolationProvider negativeOrZero(String field, Double value) {
    return () -> negativeOrZeroRule(field, value);
  }

  public static Optional<Violation> negativeOrZeroRule(String field, Double value) {
    return negativeOrZeroRule(field, () -> value <= 0, value);
  }

  public static ViolationProvider negativeOrZero(String field, Float value) {
    return () -> negativeOrZeroRule(field, value);
  }

  public static Optional<Violation> negativeOrZeroRule(String field, Float value) {
    return negativeOrZeroRule(field, () -> value <= 0, value);
  }

  public static ViolationProvider negativeOrZero(String field, Long value) {
    return () -> negativeOrZeroRule(field, value);
  }

  public static Optional<Violation> negativeOrZeroRule(String field, Long value) {
    return negativeOrZeroRule(field, () -> value <= 0, value);
  }

  public static ViolationProvider negativeOrZero(String field, Integer value) {
    return () -> negativeOrZeroRule(field, value);
  }

  public static Optional<Violation> negativeOrZeroRule(String field, Integer value) {
    return negativeOrZeroRule(field, () -> value <= 0, value);
  }

  public static ViolationProvider negative(String field, BigDecimal value) {
    return () -> negativeRule(field, value);
  }

  public static Optional<Violation> negativeRule(String field, BigDecimal value) {
    return negativeRule(field, () -> value.compareTo(BigDecimal.ZERO) < 0, value);
  }

  private static Optional<Violation> negativeRule(
          String field, BooleanSupplier conditionProvider, Object value) {
    return commonPositiveAndNegative(
            conditionProvider,
            value,
            Violation.of(
                    field,
                    "validation.error.positive.or.zero.value",
                    "The value must be a negative number (zero not allowed)."));
  }

  public static ViolationProvider negative(String field, BigInteger value) {
    return () -> negativeRule(field, value);
  }

  public static Optional<Violation> negativeRule(String field, BigInteger value) {
    return negativeRule(field, () -> value.compareTo(BigInteger.ZERO) < 0, value);
  }

  public static ViolationProvider negative(String field, Double value) {
    return () -> negativeRule(field, value);
  }

  public static Optional<Violation> negativeRule(String field, Double value) {
    return negativeRule(field, () -> value < 0, value);
  }

  public static ViolationProvider negative(String field, Float value) {
    return () -> negativeRule(field, value);
  }

  public static Optional<Violation> negativeRule(String field, Float value) {
    return negativeRule(field, () -> value < 0, value);
  }

  public static ViolationProvider negative(String field, Long value) {
    return () -> negativeRule(field, value);
  }

  public static Optional<Violation> negativeRule(String field, Long value) {
    return negativeRule(field, () -> value < 0, value);
  }

  public static ViolationProvider negative(String field, Integer value) {
    return () -> negativeRule(field, value);
  }

  public static Optional<Violation> negativeRule(String field, Integer value) {
    return negativeRule(field, () -> value < 0, value);
  }

  /**
   * Checks if the value is smaller than or equals to the provided superior limit. If the value is
   * null then no violation is returned.
   *
   * @param field path to field
   * @param value value to be checked
   * @param max superior limit
   * @return violation or success
   */
  public static ViolationProvider max(String field, Integer value, int max) {
    return () -> maxRule(field, value, max);
  }

  /**
   * See {@link #max(String, Integer, int)}
   */
  public static Optional<Violation> maxRule(String field, Integer value, int max) {
    if (value == null) {
      return Optional.empty();
    }
    return isTrueRule(
            () -> value <= max,
            Violation.of(
                    field,
                    "validation.error.integer.value.greater.than.max",
                    "Value is greater than max.",
                    singletonMap("max", max)));
  }

  /**
   * Checks if the compared value is strictly after the other. If the value is null then no
   * violation is returned.
   *
   * @param field path to field
   * @param value value to be checked
   * @param other other value
   * @param <T> compared object type
   * @return violation or success
   */
  public static <T> ViolationProvider after(String field, Comparable<T> value, T other) {
    return () -> afterRule(field, value, other);
  }

  /**
   * See {@link #after(String, Comparable, Object)}
   */
  public static <T> Optional<Violation> afterRule(String field, Comparable<T> value, T other) {
    return compareComparableRule(
            value,
            other,
            (tComparable, t) -> value.compareTo(other) <= 0,
            () ->
                    Violation.of(
                            field,
                            "validation.error.value.is.before.or.equal",
                            "The value is before or equal the other value.",
                            singletonMap(PARAM_NAME_OTHER, other == null ? null : other.toString())));
  }

  /**
   * Returns violation in case compare function is true. If the value is null then no violation is
   * returned.
   *
   * @param value         first value
   * @param other         second value
   * @param compareFunc   function that compares the two values
   * @param violationFunc the Violation that will be returned
   * @return violation or success
   */
  public static <T> Optional<Violation> compareComparableRule(
          Comparable<T> value,
          T other,
          BiPredicate<Comparable<T>, T> compareFunc,
          Supplier<Violation> violationFunc) {
    if (value == null) {
      return Optional.empty();
    }
    return isTrueRule(() -> other != null && !compareFunc.test(value, other), violationFunc);
  }

  /**
   * Checks if the compared value is after or equals to the other. If the value is null then no
   * violation is returned.
   *
   * @param field path to field
   * @param value value to be checked
   * @param other other value
   * @param <T> compared object type
   * @return violation or success
   */
  public static <T> ViolationProvider afterOrEqualsTo(String field, Comparable<T> value, T other) {
    return () -> afterOrEqualsToRule(field, value, other);
  }

  /**
   * See {@link #afterOrEqualsTo(String, Comparable, Object)}
   */
  public static <T> Optional<Violation> afterOrEqualsToRule(
          String field, Comparable<T> value, T other) {
    return compareComparableRule(
            value,
            other,
            (tComparable, t) -> value.compareTo(other) < 0,
            () ->
                    Violation.of(
                            field,
                            "validation.error.value.is.before",
                            "The value is before the other value.",
                            singletonMap(PARAM_NAME_OTHER, other == null ? null : other.toString())));
  }

  /**
   * Checks if the compared value is strictly before the other. If the value is null then no
   * violation is returned.
   *
   * @param field path to field
   * @param value value to be checked
   * @param other other value
   * @param <T> compared object type
   * @return violation or success
   */
  public static <T> ViolationProvider before(String field, Comparable<T> value, T other) {
    return () -> beforeRule(field, value, other);
  }

  /**
   * See {@link #before(String, Comparable, Object)}
   */
  public static <T> Optional<Violation> beforeRule(String field, Comparable<T> value, T other) {
    return compareComparableRule(
            value,
            other,
            (tComparable, t) -> value.compareTo(other) >= 0,
            () ->
                    Violation.of(
                            field,
                            "validation.error.value.is.after.or.equal",
                            "The value is after or equal the other value.",
                            singletonMap(PARAM_NAME_OTHER, other == null ? null : other.toString())));
  }

  /**
   * Checks if the compared value is before or equals to the other. If the value is null then no
   * violation is returned.
   *
   * @param field path to field
   * @param value value to be checked
   * @param other other value
   * @param <T> compared object type
   * @return violation or success
   */
  public static <T> ViolationProvider beforeOrEqualsTo(String field, Comparable<T> value, T other) {
    return () -> beforeOrEqualsToRule(field, value, other);
  }

  /**
   * See {@link #beforeOrEqualsTo(String, Comparable, Object)}
   */
  public static <T> Optional<Violation> beforeOrEqualsToRule(
          String field, Comparable<T> value, T other) {
    return compareComparableRule(
            value,
            other,
            (tComparable, t) -> value.compareTo(other) > 0,
            () ->
                    Violation.of(
                            field,
                            "validation.error.value.is.after",
                            "The value is after the other value.",
                            singletonMap(PARAM_NAME_OTHER, other == null ? null : other.toString())));
  }

  /**
   * Checks if the compared value equals the other. If the value is null then no violation is
   * returned.
   *
   * @param field path to field
   * @param value value to be checked
   * @param other other value
   * @param <T> compared object type
   * @return violation or success
   */
  public static <T> ViolationProvider equalsTo(String field, Comparable<T> value, T other) {
    return () -> equalsToRule(field, value, other);
  }

  /**
   * See {@link #equalsTo(String, Comparable, Object)}
   */
  public static <T> Optional<Violation> equalsToRule(String field, Comparable<T> value, T other) {
    return compareComparableRule(
            value,
            other,
            (tComparable, t) -> value.compareTo(other) != 0,
            () ->
                    Violation.of(
                            field,
                            "validation.error.value.is.not.equal",
                            "The value is not equal to the other value.",
                            singletonMap(PARAM_NAME_OTHER, other == null ? null : other.toString())));
  }

  /**
   * Checks if the compared value equals the other. If the value is null then no violation is
   * returned.
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
   * See {@link #equalsTo(String, String, String)}
   */
  public static Optional<Violation> equalsToRule(String field, String value, String other) {
    return compareStringsRule(
            value,
            other,
            (s, s2) -> !value.equals(other),
            () ->
                    Violation.of(
                            field,
                            "validation.error.value.is.not.equal",
                            "The value is not equal to the other value.",
                            singletonMap(PARAM_NAME_OTHER, other)));
  }

  /**
   * Returns violation in case compare function is true. If the value is null then no violation is
   * returned.
   *
   * @param value         first value
   * @param other         second value
   * @param compareFunc   function that compares the two values
   * @param violationFunc the Violation that will be returned
   * @return violation or success
   */
  public static Optional<Violation> compareStringsRule(
          String value,
          String other,
          BiPredicate<String, String> compareFunc,
          Supplier<Violation> violationFunc) {
    if (value == null) {
      return Optional.empty();
    }
    return isFalseRule(() -> compareFunc.test(value, other), violationFunc);
  }

  /**
   * See {@link #isFalse(BooleanSupplier, Violation)}
   *
   * @param violation violation provider
   */
  public static Optional<Violation> isFalseRule(
          BooleanSupplier condition, Supplier<Violation> violation) {
    return isTrueRule(() -> !condition.getAsBoolean(), violation);
  }

  /**
   * Compares the two string against a provided rule. If the validation rule is evaluated to true
   * then the provided violation will be returned.
   *
   * @param value value to be checked
   * @param other other value
   * @param compareFunc comparison function
   * @param violationFunc violation to be retrieved when compareFunc evaluates to true
   * @return violation or success
   */
  public static ViolationProvider compareStrings(
          String value,
          String other,
          BiPredicate<String, String> compareFunc,
          Supplier<Violation> violationFunc) {
    return () -> compareStringsRule(value, other, compareFunc, violationFunc);
  }

  /**
   * Checks if the compared value is not equal to the other. If the value is null then no violation
   * is returned.
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
   * See {@link #notEqualsTo(String, String, String)}
   */
  public static Optional<Violation> notEqualsToRule(String field, String value, String other) {
    return compareStringsRule(
        value,
        other,
        (s, s2) -> value.equals(other),
        () ->
            Violation.of(
                    field,
                    "validation.error.string.is.equal",
                    "The value is equal to the other value.",
                singletonMap(PARAM_NAME_OTHER, other)));
  }

  public static <T> ViolationProvider compareComparable(
      Comparable<T> value,
      T other,
      BiPredicate<Comparable<T>, T> compareFunc,
      Supplier<Violation> violationFunc) {
    return () -> compareComparableRule(value, other, compareFunc, violationFunc);
  }
}
