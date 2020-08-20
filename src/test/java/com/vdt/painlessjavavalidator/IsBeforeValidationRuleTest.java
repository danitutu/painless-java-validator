package com.vdt.painlessjavavalidator;

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

import static com.vdt.painlessjavavalidator.ValidationRule.isBefore;
import static org.junit.jupiter.api.Assertions.*;

class IsBeforeValidationRuleTest {

    @Test
    @DisplayName("WHEN value is after other THEN expect no violation")
    public void isBefore1() {
        Map<Object, Object> map = new HashMap<>();
        map.put(7, 6);
        map.put(7.5, 7.4);
        map.put(BigInteger.valueOf(7), BigInteger.valueOf(6));
        map.put(BigDecimal.valueOf(7.5), BigDecimal.valueOf(7.4));
        map.put(Instant.now().plusSeconds(60), Instant.now());
        map.put(LocalDate.now().plusDays(1), LocalDate.now());
        map.put(LocalDateTime.now().plusSeconds(60), LocalDateTime.now());
        map.put(ZonedDateTime.now().plusSeconds(60), ZonedDateTime.now());

        map.forEach((key, value) -> {
            Optional<Violation> violation = isBefore("field.path", (Comparable<Object>) key, value);

            try {
                assertTrue(violation.isPresent());
                assertEquals("field.path", violation.get().getFieldPath());
                assertEquals("validation.error.value.is.after.or.equal", violation.get().getMessage());
                assertEquals("The value is after or equal the other value.", violation.get().getDetails());
                assertEquals(1, violation.get().getAttributes().size());
                assertEquals(value.toString(), violation.get().getAttributes().get("other"));
            } catch (Throwable t) {
                System.out.println("Validation failed for value pair [" + key + "," + value + "]");
                throw t;
            }
        });
    }

    @Test
    @DisplayName("WHEN value is before other THEN expect violation")
    public void isBefore2() {
        Map<Object, Object> map = new HashMap<>();
        map.put(5, 6);
        map.put(7.3, 7.4);
        map.put(BigInteger.valueOf(5), BigInteger.valueOf(6));
        map.put(BigDecimal.valueOf(7.3), BigDecimal.valueOf(7.4));
        map.put(Instant.now().minusSeconds(60), Instant.now());
        map.put(LocalDate.now().minusDays(1), LocalDate.now());
        map.put(LocalDateTime.now().minusSeconds(60), LocalDateTime.now());
        map.put(ZonedDateTime.now().minusSeconds(60), ZonedDateTime.now());

        map.forEach((key, value) -> {
            Optional<Violation> violation = isBefore("field.path", (Comparable<Object>) key, value);

            assertFalse(violation.isPresent(), "Validation failed for value pair [" + key + "," + value + "]");
        });
    }

    @Test
    @DisplayName("WHEN value is equal other THEN expect violation")
    public void isBefore3() {
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

        map.forEach((key, value) -> {
            Optional<Violation> violation = isBefore("field.path", (Comparable<Object>) key, value);

            try {
                assertTrue(violation.isPresent());
                assertEquals("field.path", violation.get().getFieldPath());
                assertEquals("validation.error.value.is.after.or.equal", violation.get().getMessage());
                assertEquals("The value is after or equal the other value.", violation.get().getDetails());
                assertEquals(1, violation.get().getAttributes().size());
                assertEquals(value.toString(), violation.get().getAttributes().get("other"));
            } catch (Throwable t) {
                System.out.println("Validation failed for value pair [" + key + "," + value + "]");
                throw t;
            }
        });
    }

    @Test
    @DisplayName("WHEN value is null THEN expect violation")
    public void isBefore4() {
        Optional<Violation> violation = isBefore("field.path", null, null);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.value.is.required", violation.get().getMessage());
        assertEquals("The value is required.", violation.get().getDetails());
    }

    @Test
    @DisplayName("WHEN other is null THEN expect violation")
    public void isBefore5() {
        Optional<Violation> violation = isBefore("field.path", Instant.now(), null);

        assertTrue(violation.isPresent());
        assertEquals("field.path", violation.get().getFieldPath());
        assertEquals("validation.error.value.is.after.or.equal", violation.get().getMessage());
        assertEquals("The value is after or equal the other value.", violation.get().getDetails());
        assertEquals(1, violation.get().getAttributes().size());
        assertNull(violation.get().getAttributes().get("other"));
    }

}