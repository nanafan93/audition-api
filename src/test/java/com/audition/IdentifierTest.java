package com.audition;

import com.audition.model.Identifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class IdentifierTest {

    @Test
    void negativeIdImpossible() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Identifier("-2"));
    }

    @Test
    void whenIdNaNThenError() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Identifier("2,"));
    }

    @Test
    void emptyIdImpossible() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Identifier(""));
    }

    @Test
    void blankIdImpossible() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Identifier("  "));
    }

    @Test
    void floatIdImpossible() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Identifier("1.2"));
    }
}
