package com.github.paniclab.domain;

import org.junit.jupiter.api.Test;
import test.Customer;
import test.HaoticEnum;

import java.util.*;

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
    void arraysTest() {

        Long[] one = new Long[3];
        Long[] two = new Long[3];

        for (int x=0; x<3; x++ ) {
            one[x] = (long)x;
            two[x] = (long)x;
        }

        assertTrue(Arrays.equals(one,two));
        assertTrue(Arrays.deepEquals(one, two));
        assertFalse(one == two);

        two[2] = 100L;

        assertFalse(Arrays.equals(one,two));
        assertFalse(Arrays.deepEquals(one,two));
    }

    @Test
    void collectionTest() {
        List<String> list = new ArrayList<>();

        assertTrue(Collection.class.isAssignableFrom(list.getClass()));

    }

    @Test
    void getCopyOf_() {

        Customer original = new Customer();
        original.setAge(32);
        original.setName("Влад");
        original.setHaoticEnum(HaoticEnum.THREE);

        Long[] array = new Long[3];
        for (int x=0; x<3; x++ ) {
            array[x] = (long)x*10;
        }
        original.setArray(array);

        //List<String> list = Arrays.asList("Alpha", "Beta", "Gamma");
        List<String> list = new ArrayList<>();
        list.add("Alpha");
        list.add("Beta");
        list.add("Gamma");
        original.setList(list);

        Set<Integer> set = new HashSet<>();
        set.add(100);
        set.add(200);
        set.add(300);
        original.setSet(set);

        Customer copy = Entities.getCopyOf(original);

        assertEquals(original, copy);
        assertFalse(original == copy);

        assertTrue(Arrays.deepHashCode(original.getArray()) == Arrays.deepHashCode(copy.getArray()));


    }


}