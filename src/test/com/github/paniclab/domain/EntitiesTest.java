package com.github.paniclab.domain;

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
}