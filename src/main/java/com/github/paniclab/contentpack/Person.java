package com.github.paniclab.contentpack;

import java.util.Collection;

public class Person implements Convertible{
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    //@SuppressWarnings("unchecked")
    public <T> T convertTo(Class<? extends T> clazz) {
        if(clazz == String.class) {
            return clazz.cast(convertToString());
        }
        return Convertible.super.convertTo(clazz);
    }

    private String convertToString() {
        return null;
    }
}
