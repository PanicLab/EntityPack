package com.github.paniclab.contentpack;

/*
*   Пример того, как можно переопределить метод T convertTo(Class<T> clazz)
*
*    public <T> T convertTo(Class<T> clazz) {
*        if(clazz == String.class) {
*            return clazz.cast(convertToString());
*        }
*        return Convertible.super.convertTo(clazz);
*    }
*
*
* */

public interface Convertible {
    default <T> T convertTo(Class<? extends T> clazz) {
        if(clazz.isAssignableFrom(this.getClass())) {
            return clazz.cast(this);
        }

        throw new IllegalArgumentException("Instance of " + this.getClass().getCanonicalName() +
                " can not be converted to " + clazz.getCanonicalName());
    }
}
