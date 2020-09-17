package com.github.danitutu.painlessjavavalidator;

import java.util.List;

import static java.util.Collections.singletonList;

public class ValidationException extends RuntimeException {

  private final List<Violation> violations;

  public ValidationException(Violation violation) {
    this(singletonList(violation));
  }

  public ValidationException(List<Violation> violations) {
    this.violations = violations;
  }

  /**
   * @param violation violation; null is accepted
   */
  public static void stopIfViolation(Violation violation) {
    if (violation != null) {
      stopIfViolations(singletonList(violation));
    }
  }

  /**
   * @param violations list of violations; null is accepted
   */
  public static void stopIfViolations(List<Violation> violations) {
    if (violations != null && !violations.isEmpty()) {
      throw new ValidationException(violations);
    }
  }

  public List<Violation> getViolations() {
    return violations;
  }
}
