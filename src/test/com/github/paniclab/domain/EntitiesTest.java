package com.github.paniclab.domain;

import org.junit.jupiter.api.Test;
import test.Customer;
import test.HaoticEnum;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EntitiesTest {

    @Test
    void createDetached() {
        Customer detached = Entities.createDetached(1L, Customer.class);
        assertNotNull(detached);

        Customer otherDetached = Entity.getDetached(2L, Customer.class);
        assertNotNull(otherDetached);
    }

    @Test
    void equalsById() {
        Customer detached = Entities.createDetached(100L, Customer.class);
        assertNotNull(detached);

        Customer otherDetached = Entity.getDetached(100L, Customer.class);
        assertNotNull(otherDetached);

        assertTrue(Entities.equalsByIds(detached, otherDetached));
    }

    @Test
    void equalsByContent_contentAndIdsAreEquals_returnTrue() {
        Customer one = Entities.createDetached(100L, Customer.class);
        Customer another = Entity.getDetached(100L, Customer.class);

        one.setAge(32);
        another.setAge(32);
        //one.setName("Влад");
        //another.setName("Влад");
        one.setHaoticEnum(HaoticEnum.THREE);
        another.setHaoticEnum(HaoticEnum.THREE);

        Long[] array1 = new Long[3];
        for (int x=0; x<3; x++ ) {
            array1[x] = (long)x*10;
        }
        one.setArray(array1);

        Long[] array2 = new Long[3];
        for (int x=0; x<3; x++ ) {
            array2[x] = (long)x*10;
        }
        another.setArray(array2);

        List<String> list = Arrays.asList("Alpha", "Beta", "Gamma");
        one.setList(list);
        another.setList(list);

        Set<String> set = new TreeSet<>();
        set.add(String.valueOf(100));
        set.add(String.valueOf(200));
        set.add(String.valueOf(300));
        one.setSet(set);
        another.setSet(set);

        Collection<String> arrayDeque1 = new ArrayDeque<>();
        arrayDeque1.add("SUNDAY");
        arrayDeque1.add("MONDAY");
        arrayDeque1.add("TUESDAY");
        one.setArrayDeque(arrayDeque1);

        Collection<String> arrayDeque2 = new ArrayDeque<>();
        arrayDeque2.add("SUNDAY");
        arrayDeque2.add("MONDAY");
        arrayDeque2.add("TUESDAY");
        another.setArrayDeque(arrayDeque2);

        assertTrue(Entities.equalsByContent(one,another));
        assertTrue(one.hashCode() == another.hashCode());
    }


    @Test
    void equalsByContent_contentIsEqualsAndIdsAreNot_returnFalse() {
        Customer one = Entities.createDetached(100L, Customer.class);
        Customer another = Entity.getDetached(200L, Customer.class);

        one.setAge(32);
        another.setAge(32);
        //one.setName("Влад");
        //another.setName("Влад");
        one.setHaoticEnum(HaoticEnum.THREE);
        another.setHaoticEnum(HaoticEnum.THREE);

        Long[] array1 = new Long[3];
        for (int x=0; x<3; x++ ) {
            array1[x] = (long)x*10;
        }
        one.setArray(array1);

        Long[] array2 = new Long[3];
        for (int x=0; x<3; x++ ) {
            array2[x] = (long)x*10;
        }
        another.setArray(array2);

        List<String> list = Arrays.asList("Alpha", "Beta", "Gamma");
        one.setList(list);
        another.setList(list);

        Set<String> set = new TreeSet<>();
        set.add(String.valueOf(100));
        set.add(String.valueOf(200));
        set.add(String.valueOf(300));
        one.setSet(set);
        another.setSet(set);

        Collection<String> arrayDeque1 = new ArrayDeque<>();
        arrayDeque1.add("SUNDAY");
        arrayDeque1.add("MONDAY");
        arrayDeque1.add("TUESDAY");
        one.setArrayDeque(arrayDeque1);

        Collection<String> arrayDeque2 = new ArrayDeque<>();
        arrayDeque2.add("SUNDAY");
        arrayDeque2.add("MONDAY");
        arrayDeque2.add("TUESDAY");
        another.setArrayDeque(arrayDeque2);

        assertFalse(Entities.equalsByContent(one,another));
        assertFalse(one.hashCode() == another.hashCode());
    }

    @Test
    void equalsByContentExceptIds_contentIsEqualsAndIdsAreNot_returnTrue() {
        Customer one = Entities.createDetached(100L, Customer.class);
        Customer another = Entity.getDetached(200L, Customer.class);

        one.setAge(32);
        another.setAge(32);
        //one.setName("Влад");
        //another.setName("Влад");
        one.setHaoticEnum(HaoticEnum.THREE);
        another.setHaoticEnum(HaoticEnum.THREE);

        Long[] array1 = new Long[3];
        for (int x=0; x<3; x++ ) {
            array1[x] = (long)x*10;
        }
        one.setArray(array1);

        Long[] array2 = new Long[3];
        for (int x=0; x<3; x++ ) {
            array2[x] = (long)x*10;
        }
        another.setArray(array2);

        List<String> list = Arrays.asList("Alpha", "Beta", "Gamma");
        one.setList(list);
        another.setList(list);

        Set<String> set = new TreeSet<>();
        set.add(String.valueOf(100));
        set.add(String.valueOf(200));
        set.add(String.valueOf(300));
        one.setSet(set);
        another.setSet(set);

        Collection<String> arrayDeque1 = new ArrayDeque<>();
        arrayDeque1.add("SUNDAY");
        arrayDeque1.add("MONDAY");
        arrayDeque1.add("TUESDAY");
        one.setArrayDeque(arrayDeque1);

        Collection<String> arrayDeque2 = new ArrayDeque<>();
        arrayDeque2.add("SUNDAY");
        arrayDeque2.add("MONDAY");
        arrayDeque2.add("TUESDAY");
        another.setArrayDeque(arrayDeque2);

        assertFalse(Entities.equalsByIds(one, another));
        assertTrue(Entities.equalsByContentExceptId(one,another));
        assertFalse(one.hashCode() == another.hashCode());
    }

    @Test
    void equalsByContentExceptIds_contentAndIdsAreEquals_returnTrue() {
        Customer one = Entities.createDetached(200L, Customer.class);
        Customer another = Entity.getDetached(200L, Customer.class);

        one.setAge(32);
        another.setAge(32);
        //one.setName("Влад");
        //another.setName("Влад");
        one.setHaoticEnum(HaoticEnum.THREE);
        another.setHaoticEnum(HaoticEnum.THREE);

        Long[] array1 = new Long[3];
        for (int x=0; x<3; x++ ) {
            array1[x] = (long)x*10;
        }
        one.setArray(array1);

        Long[] array2 = new Long[3];
        for (int x=0; x<3; x++ ) {
            array2[x] = (long)x*10;
        }
        another.setArray(array2);

        List<String> list = Arrays.asList("Alpha", "Beta", "Gamma");
        one.setList(list);
        another.setList(list);

        Set<String> set = new TreeSet<>();
        set.add(String.valueOf(100));
        set.add(String.valueOf(200));
        set.add(String.valueOf(300));
        one.setSet(set);
        another.setSet(set);

        Collection<String> arrayDeque1 = new ArrayDeque<>();
        arrayDeque1.add("SUNDAY");
        arrayDeque1.add("MONDAY");
        arrayDeque1.add("TUESDAY");
        one.setArrayDeque(arrayDeque1);

        Collection<String> arrayDeque2 = new ArrayDeque<>();
        arrayDeque2.add("SUNDAY");
        arrayDeque2.add("MONDAY");
        arrayDeque2.add("TUESDAY");
        another.setArrayDeque(arrayDeque2);

        assertTrue(Entities.equalsByIds(one, another));
        assertTrue(Entities.equalsByContentExceptId(one,another));
        assertTrue(one.hashCode() == another.hashCode());
    }

}