package com.github.paniclab.entitypack;


import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;


public final class Entities {
    @SuppressWarnings("unchecked")
    public static <ID extends Serializable, T extends Entity<ID>> T createDetached(ID id, Class<T> clazz) {
        if (id == null) throw new NullPointerException("Attempt to create detached instance with id = null.");

        Constructor<T> constructor;

        constructor = (Constructor<T>) Arrays.stream(clazz.getDeclaredConstructors())
                .filter(c -> c.getGenericParameterTypes().length == 0)
                .findAny()
                .orElseThrow(() -> new Entity.InternalException("Unable to create instance of class " +
                        clazz.getCanonicalName() + ". This class has no appropriate " +
                        "constructor with no args."));

        T entity;
        try {
            constructor.setAccessible(true);
            entity = constructor.newInstance();

            Field idField = null;
            for (Field field : entity.getClass().getDeclaredFields()) {
                if(field.isAnnotationPresent(javax.persistence.Id.class)) {
                    idField = field;
                    break;
                }
                if(field.isAnnotationPresent(javax.persistence.EmbeddedId.class)) {
                    idField = field;
                    break;
                }
            }
            if(idField == null) {
                throw new Entity.InternalException("Id field not found. Entity:" + entity.toString());
            }

            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new Entity.InternalException("Unable to create instance of class " + clazz.getCanonicalName(), e);
        }

        return entity;
    }
}
