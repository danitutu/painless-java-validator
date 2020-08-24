package com.github.danitutu.painlessjavavalidator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ViolationTest {

    @Test
    @DisplayName("WHEN inputs are equal THEN equal")
    void equals1() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("a", "b", "c", singletonMap("1", 2));

        assertEquals(violation1, violation2);
    }

    @Test
    @DisplayName("WHEN fieldPath differ THEN not equal")
    void equals2() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("b", "b", "c", singletonMap("1", 2));

        assertNotEquals(violation1, violation2);
    }

    @Test
    @DisplayName("WHEN message differ THEN not equal")
    void equals3() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("a", "c", "c", singletonMap("1", 2));

        assertNotEquals(violation1, violation2);
    }

    @Test
    @DisplayName("WHEN details differ THEN not equal")
    void equals4() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("a", "b", "d", singletonMap("1", 2));

        assertNotEquals(violation1, violation2);
    }

    @Test
    @DisplayName("WHEN params values differ THEN not equal")
    void equals5() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("a", "b", "c", singletonMap("1", 3));

        assertNotEquals(violation1, violation2);
    }

    @Test
    @DisplayName("WHEN params keys differ THEN not equal")
    void equals6() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("a", "b", "c", singletonMap("2", 2));

        assertNotEquals(violation1, violation2);
    }

    @Test
    @DisplayName("WHEN params references differ but same values THEN not equal")
    void equals7() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 2);

        Violation violation1 = Violation.of("a", "b", "c", map);
        Violation violation2 = Violation.of("a", "b", "c", singletonMap("1", 2));

        assertEquals(violation1, violation2);
    }

    @Test
    @DisplayName("WHEN inputs are equal THEN equal hashcode")
    void hashCode1() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("a", "b", "c", singletonMap("1", 2));

        assertEquals(violation1.hashCode(), violation2.hashCode());
    }

    @Test
    @DisplayName("WHEN field names differ THEN different hashcode")
    void hashCode2() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("b", "b", "c", singletonMap("1", 2));

        assertNotEquals(violation1.hashCode(), violation2.hashCode());
    }

    @Test
    @DisplayName("WHEN messages differ THEN different hashcode")
    void hashCode3() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("a", "c", "c", singletonMap("1", 2));

        assertNotEquals(violation1.hashCode(), violation2.hashCode());
    }

    @Test
    @DisplayName("WHEN details differ THEN different hashcode")
    void hashCode4() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("a", "b", "d", singletonMap("1", 2));

        assertNotEquals(violation1.hashCode(), violation2.hashCode());
    }

    @Test
    @DisplayName("WHEN params values differ THEN different hashcode")
    void hashCode5() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("a", "b", "c", singletonMap("1", 3));

        assertNotEquals(violation1.hashCode(), violation2.hashCode());
    }

    @Test
    @DisplayName("WHEN params keys differ THEN different hashcode")
    void hashCode6() {
        Violation violation1 = Violation.of("a", "b", "c", singletonMap("1", 2));
        Violation violation2 = Violation.of("a", "b", "c", singletonMap("2", 2));

        assertNotEquals(violation1.hashCode(), violation2.hashCode());
    }

    @Test
    @DisplayName("WHEN params references differ but same values THEN equal hashcode")
    void hashCode7() {
        Map<String, Object> map = new HashMap<>();
        map.put("1", 2);

        Violation violation1 = Violation.of("a", "b", "c", map);
        Violation violation2 = Violation.of("a", "b", "c", singletonMap("1", 2));

        assertEquals(violation1.hashCode(), violation2.hashCode());
    }
}