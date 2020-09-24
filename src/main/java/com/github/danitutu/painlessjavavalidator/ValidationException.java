package com.github.danitutu.painlessjavavalidator;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class ValidationException extends RuntimeException {

  private final List<Violation> violations;

  public ValidationException(Violation violation) {
    this(singletonList(violation));
  }

  public ValidationException(List<Violation> violations) {
    super("Violations: " + printViolations(violations));
    this.violations = violations;
  }

  public static String printViolations(List<Violation> violations) {
    if (violations == null) {
      violations = emptyList();
    }
    return "["
            + violations.stream()
            .map(
                    violation ->
                            "{"
                                    + "field='"
                                    + violation.getField()
                                    + '\''
                                    + ", message='"
                                    + violation.getMessage()
                                    + '\''
                                    + ", details='"
                                    + violation.getDetails()
                                    + '\''
                                    + ", attributes="
                                    + violation.getAttributes()
                                    + '}')
            .collect(Collectors.joining(", "))
            + "]";
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
