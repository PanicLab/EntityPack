package com.github.paniclab.domain;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public interface Entity<T extends Serializable> {

    @SuppressWarnings("unchecked")
    static <ID extends Serializable, TYPE extends Entity<ID>> TYPE getDetached(ID id, Class<TYPE> clazz) {
        if (id == null) throw new NullPointerException("Attempt to create detached instance with id = null.");

        Constructor<TYPE> constructor;
        constructor = (Constructor<TYPE>) Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.getGenericParameterTypes().length == 0)
                .findAny()
                .orElseThrow(() -> new InternalException("Unable to create instance of class " +
                        clazz.getCanonicalName() + ". This class has no appropriate " +
                        "constructor with no args."));

        TYPE entity;
        try {
            constructor.setAccessible(true);
            entity = constructor.newInstance();
            //TODO сначала попробовать найти аннотированные поля @Id и т.д.
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |NoSuchFieldException e) {
            throw new InternalException("Unable to create instance of class " + clazz.getCanonicalName(), e);
        }

        return entity;
    }


    T getId();

    Entity<T> getThis();

    default boolean isTransient() {
        return !isAlreadyPersisted();
    }

    default boolean isAlreadyPersisted() {
        return getId() != null;
    }

    default Class<? extends Entity> getEntityClass() {
        return getThis().getClass();
    }

    default boolean hasSameContentExceptIdAs(Entity<T> another) {
        return Entities.hasSameContentExceptId(getThis(), another);
    }

    default boolean hasSameContentAs(Entity<T> another) {
        return Entities.hasSameContent(getThis(), another);
    }

    default boolean hasSameIdAs(Entity<T> another) {
        return Entities.hasEqualsId(getThis(), another);
    }

    class InternalException extends RuntimeException {
        public InternalException() {
        }

        InternalException(String message) {
            super(message);
        }

        InternalException(String message, Throwable cause) {
            super(message, cause);
        }

        InternalException(Throwable cause) {
            super(cause);
        }

        InternalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
