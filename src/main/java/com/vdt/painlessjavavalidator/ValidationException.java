package com.vdt.painlessjavavalidator;

import java.util.List;

import static java.util.Collections.singletonList;

public class ValidationException extends RuntimeException {

    private final List<Violation> violations;

    public ValidationException(List<Violation> violations) {
        this.violations = violations;
    }

    public ValidationException(Violation violation) {
        this(singletonList(violation));
    }

    public List<Violation> getViolations() {
        return violations;
    }

    /**
     * @param violations list of violations; null is accepted
     */
    public static void stopIfViolations(List<Violation> violations) {
        if (violations != null && !violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }

    /**
     * @param violation violation; null is accepted
     */
    public static void stopIfViolation(Violation violation) {
        if (violation != null) {
            stopIfViolations(singletonList(violation));
        }
    }
}
