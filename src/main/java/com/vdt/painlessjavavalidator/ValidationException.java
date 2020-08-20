package com.vdt.painlessjavavalidator;

import java.util.List;

import static java.util.Collections.singletonList;

public class ValidationException extends RuntimeException {

    private List<Violation> violations;

    public ValidationException(List<Violation> violations) {
        this.violations = violations;
    }

    public ValidationException(Violation violation) {
        this(singletonList(violation));
    }

    public List<Violation> getViolations() {
        return violations;
    }
}
