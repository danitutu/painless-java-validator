package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.github.danitutu.painlessjavavalidator.ValidationRule.notBlank;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ViolationProviderUtilsTest {

  @Test
  @DisplayName("WHEN input is null THEN empty list")
  void from1() {
    List<ViolationProvider> list = ViolationProviderUtils.from(null, (i, o) -> null);
    assertEquals(0, list.size());
  }

  @Test
  @DisplayName("WHEN mapper is null THEN empty list")
  void from2() {
    IllegalArgumentException ex =
            assertThrows(
                    IllegalArgumentException.class, () -> ViolationProviderUtils.from(emptyList(), null));

    assertEquals("mapper is required", ex.getMessage());
  }

  @Test
  @DisplayName("WHEN collection is empty THEN empty list")
  void from3() {
    List<ViolationProvider> list = ViolationProviderUtils.from(emptyList(), (i, o) -> null);
    assertEquals(0, list.size());
  }

  @Test
  @DisplayName("WHEN input valid THEN for each element we must get a violation provider")
  void from4() {
    List<String> list = new LinkedList<>();
    list.add("   ");
    list.add("s");
    list.add("b");

    List<ViolationProvider> result =
            ViolationProviderUtils.from(list, (i, s) -> notBlank(String.format("input[%d]", i), s));

    assertEquals(3, result.size());

    long violations = result.stream().map(Supplier::get).filter(Optional::isPresent).count();

    assertEquals(1, violations);
  }
}
