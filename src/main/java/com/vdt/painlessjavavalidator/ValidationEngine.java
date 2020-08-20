package com.vdt.painlessjavavalidator;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class ValidationEngine {

    public static List<Violation> validate(Supplier<Optional<Violation>> rule) {
        return validate(singletonList(rule));
    }

    public static List<Violation> validate(List<Supplier<Optional<Violation>>> rules) {
        return rules
                .stream()
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

}
