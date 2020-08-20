package com.vdt.painlessjavavalidator;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ValidationEngine {

    public static List<Violation> validate(ViolationProvider... rules) {
        return Stream.of(rules)
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

}
