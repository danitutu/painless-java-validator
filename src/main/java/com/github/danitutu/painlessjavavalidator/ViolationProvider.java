package com.github.danitutu.painlessjavavalidator;

import java.util.Optional;
import java.util.function.Supplier;

public interface ViolationProvider extends Supplier<Optional<Violation>> {
}
