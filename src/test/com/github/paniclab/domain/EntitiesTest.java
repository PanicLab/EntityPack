package com.github.paniclab.domain;

import org.junit.jupiter.api.Test;
import test.Customer;

import static org.junit.jupiter.api.Assertions.*;

class EntitiesTest {

    @org.junit.jupiter.api.Test
    void getCopyOf_copyEmptyInstance_equalsReturnTrueAndIdentityReturnFalse() {

        Customer original = new Customer();

        Customer copy = Entities.getCopyOf(original);

        assertEquals(original, copy);
        assertFalse(original == copy);
    }

    @Test
    void getCopyOf_() {

        Customer original = new Customer();
        original.setAge(32);
        original.setName("Влад");

        Customer copy = Entities.getCopyOf(original);

        assertEquals(original, copy);
        assertFalse(original == copy);

        copy.setName("Илья");

        assertNotEquals(original, copy);
    }
}