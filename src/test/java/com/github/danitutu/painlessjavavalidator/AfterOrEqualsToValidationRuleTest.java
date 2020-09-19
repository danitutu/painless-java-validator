package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.danitutu.painlessjavavalidator.TestUtils.assertViolationIsBefore;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AfterOrEqualsToValidationRuleTest {

  @Test
  @DisplayName("WHEN value is after other THEN expect no violation")
  void afterOrEqualsTo1() {
    Map<Object, Object> map = new HashMap<>();
    map.put(7, 6);
    map.put(7.5, 7.4);
    map.put(BigInteger.valueOf(7), BigInteger.valueOf(6));
    map.put(BigDecimal.valueOf(7.5), BigDecimal.valueOf(7.4));
    map.put(Instant.now().plusSeconds(60), Instant.now());
    map.put(LocalDate.now().plusDays(1), LocalDate.now());
    map.put(LocalDateTime.now().plusSeconds(60), LocalDateTime.now());
    map.put(ZonedDateTime.now().plusSeconds(60), ZonedDateTime.now());

    map.forEach(
            (key, value) -> {
              Optional<Violation> violation =
                      ValidationRule.afterOrEqualsToRule("field.path", (Comparable<Object>) key, value);

              assertFalse(
                      violation.isPresent(),
                      "Validation failed for value pair [" + key + "," + value + "]");
            });
  }

  @Test
  @DisplayName("WHEN value is before other THEN expect violation")
  void afterOrEqualsTo2() {
    Map<Object, Object> map = new HashMap<>();
    map.put(5, 6);
    map.put(7.3, 7.4);
    map.put(BigInteger.valueOf(5), BigInteger.valueOf(6));
    map.put(BigDecimal.valueOf(7.3), BigDecimal.valueOf(7.4));
    map.put(Instant.now().minusSeconds(60), Instant.now());
    map.put(LocalDate.now().minusDays(1), LocalDate.now());
    map.put(LocalDateTime.now().minusSeconds(60), LocalDateTime.now());
    map.put(ZonedDateTime.now().minusSeconds(60), ZonedDateTime.now());

    map.forEach(
            (key, value) -> {
              Optional<Violation> violation =
                      ValidationRule.afterOrEqualsToRule("field.path", (Comparable<Object>) key, value);

              try {
                assertTrue(violation.isPresent());
                assertViolationIsBefore(violation.get(), "field.path", value.toString());
              } catch (Throwable t) {
                System.out.println("Validation failed for value pair [" + key + "," + value + "]");
                throw t;
              }
            });
  }

  @Test
  @DisplayName("WHEN value is equal other THEN expect violation")
  void afterOrEqualsTo3() {
    Map<Object, Object> map = new HashMap<>();
    map.put(6, 6);
    map.put(7.4, 7.4);
    map.put(BigInteger.valueOf(6), BigInteger.valueOf(6));
    map.put(BigDecimal.valueOf(7.4), BigDecimal.valueOf(7.4));
    Instant nowInstant = Instant.now();
    map.put(nowInstant, nowInstant);
    LocalDate nowLocalDate = LocalDate.now();
    map.put(nowLocalDate, nowLocalDate);
    LocalDateTime nowLocalDateTime = LocalDateTime.now();
    map.put(nowLocalDateTime, nowLocalDateTime);
    ZonedDateTime nowZonedDateTime = ZonedDateTime.now();
    map.put(nowZonedDateTime, nowZonedDateTime);

    map.forEach(
            (key, value) -> {
              Optional<Violation> violation =
                      ValidationRule.afterOrEqualsToRule("field.path", (Comparable<Object>) key, value);

              assertFalse(violation.isPresent());
            });
  }

  @Test
  @DisplayName("WHEN value is null THEN expect no violation")
  void afterOrEqualsTo4() {
    Optional<Violation> violation = ValidationRule.afterOrEqualsToRule("field.path", null, null);

    assertFalse(violation.isPresent());
  }

  @Test
  @DisplayName("WHEN other is null THEN expect violation")
  void afterOrEqualsTo5() {
    Optional<Violation> violation =
            ValidationRule.afterOrEqualsToRule("field.path", Instant.now(), null);

    assertTrue(violation.isPresent());
    assertViolationIsBefore(violation.get(), "field.path", null);
  }
}
