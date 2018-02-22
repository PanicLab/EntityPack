package com.github.paniclab.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import test.Customer;
import test.HaoticEnum;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EntitiesTest {

    @org.junit.jupiter.api.Test
    void getCopyOf_copyEmptyInstance_equalsReturnTrueAndIdentityReturnFalse() {

        Customer original = new Customer();

        Customer copy = Entities.getCopy(original);

        assertEquals(original, copy);
        assertTrue(original.hashCode() == copy.hashCode());
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
    void getCopyOf_instanceWithEnumField() {
        Customer original = new Customer();
        original.setHaoticEnum(HaoticEnum.THREE);
        Customer copy = Entities.getCopy(original);

        assertEquals(original, copy);
        assertTrue(original.hashCode() == copy.hashCode());
        assertFalse(original == copy);
    }

    @Test
    void getCopyOf_instanceWithArrayField() {
        Customer original = new Customer();

        Long[] array = new Long[3];
        for (int x=0; x<3; x++ ) {
            array[x] = (long)x*10;
        }
        original.setArray(array);

        Customer copy = Entities.getCopy(original);

        assertEquals(original, copy);
        assertTrue(original.hashCode() == copy.hashCode());
        assertFalse(original == copy);
    }

    @Test
    void getCopyOf_instanceWithConventionalList() {
        Customer original = new Customer();

        List<String> list = new ArrayList<>();
        list.add("Alpha");
        list.add("Beta");
        list.add("Gamma");
        original.setList(list);

        Customer copy = Entities.getCopy(original);

        assertEquals(original, copy);
        assertTrue(original.hashCode() == copy.hashCode());
        assertFalse(original == copy);
    }

    @Test
    void getCopyOf_instanceWithArraysAsList() {
        Customer original = new Customer();
        List<String> list = Arrays.asList("Alpha", "Beta", "Gamma");
        original.setList(list);

        Customer copy = Entities.getCopy(original);

        assertEquals(original, copy);
        assertTrue(original.hashCode() == copy.hashCode());
        assertFalse(original == copy);
    }

    @Test
    @DisplayName("Копирует стандартную Set.")
    void getCopyOf_instanceWithConventionalSet() {
        Customer original = new Customer();

        Set<String> set = new TreeSet<>();
        set.add(String.valueOf(100));
        set.add(String.valueOf(200));
        set.add(String.valueOf(300));
        original.setSet(set);

        Customer copy = Entities.getCopy(original);

        assertEquals(original, copy);
        assertTrue(original.hashCode() == copy.hashCode());
        assertFalse(original == copy);
    }

    @Disabled("Пропущен тест, который копирует Set, полученный вызовом map.keySet()")
    @DisplayName("Копирует Set, полученный вызовом map.keySet()")
    @Test
    void getCopyOf_instanceWithNonConventionalSet() {
        Customer original = new Customer();

        Map<String,Integer> map = new TreeMap<>();
        map.put("ONE", 1);
        map.put("TWO", 2);
        map.put("THREE", 3);
        Set<String> keys = map.keySet();

        original.setSet(keys);

        Customer copy = Entities.getCopy(original);

        assertEquals(original, copy);
        assertTrue(original.hashCode() == copy.hashCode());
        assertFalse(original == copy);

    }

    @Test
    void getCopyOf_instanceWithArrayDequeCollection() {

        Customer original = new Customer();

        ArrayDeque<String> deque = new ArrayDeque<>();
        deque.add("ONE");
        deque.add("TWO");
        deque.add("THREE");
        original.setArrayDeque(deque);

        Customer copy = Entities.getCopy(original);

        assertEquals(original, copy);
        assertTrue(original.hashCode() == copy.hashCode());
        assertFalse(original == copy);
    }

    @Test
    void getCopy_instanceWithHashMap() {
        Customer original = new Customer();

        Map<String, Long> map = new HashMap<>();

        map.put("ONE", 1L);
        map.put("TWO", 2L);
        map.put("THREE", 3L);
        original.setMap(map);

        Customer copy = Entities.getCopy(original);

        assertEquals(original, copy);
        assertTrue(original.hashCode() == copy.hashCode());
        assertFalse(original == copy);
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
        String[] emptyArray = new String[0];
        List<String> list = Arrays.asList(emptyArray);
/*        List<String> list = new ArrayList<>();
        list.add("Alpha");
        list.add("Beta");
        list.add("Gamma");*/
        original.setList(list);

        Set<String> set = new TreeSet<>();
        set.add(String.valueOf(100));
        set.add(String.valueOf(200));
        set.add(String.valueOf(300));
        original.setSet(set);

        Customer copy = Entities.getCopy(original);

        assertEquals(original, copy);
        assertTrue(original.hashCode() == copy.hashCode());
        assertFalse(original == copy);

        assertTrue(Arrays.deepHashCode(original.getArray()) == Arrays.deepHashCode(copy.getArray()));
        assertFalse(original.getArray() == copy.getArray());
    }

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
    void equalsByContent() {
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
}