package com.github.danitutu.painlessjavavalidator;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class ValidationEngine {

  private ValidationEngine() {
  }

  /**
   * Validates all rules and throw {@link ValidationException} if there are any violations.
   *
   * @param rules rules
   * @throws ValidationException thrown in case violations are found
   */
  public static void validateAllAndStopIfViolations(ViolationProvider... rules) {
    if (rules == null) {
      return;
    }
    validateAllAndStopIfViolations(asList(rules));
  }

  /**
   * Validates all rules and throw {@link ValidationException} if there are any violations.
   *
   * @param rules rules
   * @throws ValidationException thrown in case violations are found
   */
  public static void validateAllAndStopIfViolations(Collection<ViolationProvider> rules) {
    ValidationException.stopIfViolations(validateAll(rules));
  }

  /**
   * Validates all rules and return violations at the end.
   *
   * @param rules rules
   * @return violations
   */
  public static List<Violation> validateAll(Collection<ViolationProvider> rules) {
    return streamRulesAndKeepFoundViolations(rules).map(Optional::get).collect(toList());
  }

  /**
   * Creates a stream from the provided rules, execute the provided function and send down the
   * stream only the violations (abandon empty optionals)
   *
   * @param rules rules, not null
   * @return violations
   */
  private static Stream<Optional<Violation>> streamRulesAndKeepFoundViolations(
          Collection<ViolationProvider> rules) {
    return rules.stream().filter(Objects::nonNull).map(Supplier::get).filter(Optional::isPresent);
  }

  /**
   * Validates all rules and return violations at the end.
   *
   * @param rules rules
   * @return violations
   */
  public static List<Violation> validateAll(ViolationProvider... rules) {
    if (rules == null) {
      return emptyList();
    }
    return validateAll(asList(rules));
  }

  /**
   * Validates rules and stops when first violation is encountered and then immediately throw {@link
   * ValidationException}.
   *
   * @param rules rules
   * @throws ValidationException thrown in case violation is found
   */
  public static void validateFindFirstAndStopIfViolation(ViolationProvider... rules) {
    if (rules == null) {
      return;
    }
    validateFindFirstAndStopIfViolation(asList(rules));
  }

  /**
   * Validates rules and stops when first violation is encountered and then immediately throw {@link
   * ValidationException}.
   *
   * @param rules rules
   * @throws ValidationException thrown in case violation is found
   */
  public static void validateFindFirstAndStopIfViolation(Collection<ViolationProvider> rules) {
    ValidationException.stopIfViolations(validateFindFirst(rules));
  }

  /**
   * Validates all rules and stops when first violation is encountered.
   *
   * @param rules rules
   * @return single violation inside a list
   */
  public static List<Violation> validateFindFirst(Collection<ViolationProvider> rules) {
    return streamRulesAndKeepFoundViolations(rules)
            .findFirst()
            .orElse(Optional.empty())
            .map(Collections::singletonList)
            .orElse(emptyList());
  }

  /**
   * Validates all rules and stops when first violation is encountered.
   *
   * @param rules rules
   * @return single violation inside a list
   */
  public static List<Violation> validateFindFirst(ViolationProvider... rules) {
    if (rules == null) {
      return emptyList();
    }
    return validateFindFirst(asList(rules));
  }
}
