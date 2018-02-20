package com.github.paniclab.domain;


import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public final class Entities {

    private Entities() {
        throw new Entity.InternalException("This operation is not allowed.");
    }

    public static <T extends Entity> boolean hasSameContentExceptId(T one, T another) {
        int numberOfFields = one.getEntityClass().getDeclaredFields().length;
        if (another.getEntityClass().getDeclaredFields().length != numberOfFields) return false;

        Field[] fields = one.getEntityClass().getDeclaredFields();
        for (int x = 0; x < numberOfFields; x++  ) {
            Field current = fields[x];
            if (current.getName().equals("id")) continue;
            try {
                if (isNot(Objects.equals(current.get(one), current.get(another)))) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new Entity.InternalException("Unable to compare two entities. Entity: " + one +
                        System.lineSeparator() + "Another entity: " + another, e);
            }
        }

        return true;
    }


    public static <T> boolean hasSameContent(T one, T another) {
        int numberOfFields = one.getClass().getDeclaredFields().length;
        if (another.getClass().getDeclaredFields().length != numberOfFields) return false;

        Field[] fields = one.getClass().getDeclaredFields();
        for (int x = 0; x < numberOfFields; x++  ) {
            Field current = fields[x];
            try {
                if (isNot(Objects.equals(current.get(one), current.get(another)))) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new Entity.InternalException("Unable to compare two instances. Instance: " + one +
                        System.lineSeparator() + "Another instance: " + another, e);
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getCopyOf(T obj) {

        if (obj == null) return null;

        if (obj.getClass().isPrimitive()) {
            return obj;
        }

        if (    obj instanceof Long ||
                obj instanceof Integer ||
                obj instanceof Short ||
                obj instanceof Byte ||
                obj instanceof Boolean ||
                obj instanceof Character ||
                obj instanceof Float ||
                obj instanceof Double ||
                obj instanceof String ||
                obj instanceof BigInteger ||
                obj instanceof BigDecimal ||
                obj instanceof Enum
                ) {
            return obj;
        }

        if (obj.getClass().isArray()) {
            int arraySize = Array.getLength(obj);
            Class elementType = obj.getClass().getComponentType();

            T copy = (T)Array.newInstance(elementType, arraySize);
            for (int x = 0; x < arraySize; x++) {
                Array.set(copy, x, Array.get(obj, x));
            }

            return copy;
        }


        if (Collection.class.isAssignableFrom(obj.getClass())) {
            Constructor<T> constructor;
            Object constructorArgument;

            //TODO создать массив и вернуть как Arrays.asList
            if(obj.getClass().getCanonicalName().equals("java.util.Arrays.ArrayList")) {
                constructor = (Constructor<T>)Arrays.stream(obj.getClass().getDeclaredConstructors())
                        .findAny()
                        .orElseThrow(() -> new Entity.InternalException("Unable to create copy of instance. " +
                                System.lineSeparator() + "Instance: " + obj));
                constructorArgument = ((Collection)obj).toArray();

            } else {
                constructor = (Constructor<T>) Arrays.stream(obj.getClass().getDeclaredConstructors())
                        .filter(c -> c.getGenericParameterTypes().length == 1)
                        .filter(c -> isNot(c.getGenericParameterTypes()[0].equals(int.class)))
                        .filter(c -> ((ParameterizedType) c.getGenericParameterTypes()[0]).getRawType().equals(Collection.class))
                        .findAny()
                        .orElseThrow(() -> new Entity.InternalException("Unable to create copy of instance. " +
                                System.lineSeparator() + "Instance: " + obj));

                constructorArgument = obj;
            }

            T copy;
            constructor.setAccessible(true);
            try {
                copy = constructor.newInstance(constructorArgument);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new Entity.InternalException("Unable to create copy of instance. " +
                        System.lineSeparator() + "Instance: " + obj);
            }

            return copy;
        }

        if (Map.class.isAssignableFrom(obj.getClass())) {
            Constructor<T> constructor;
            constructor = (Constructor<T>) Arrays.stream(obj.getClass().getDeclaredConstructors())
                    .filter(c -> c.getGenericParameterTypes().length == 1)
                    .filter(c -> c.getGenericParameterTypes()[0] == Map.class)
                    .findAny()
                    .orElseThrow(() -> new Entity.InternalException("Unable to create copy of instance. " +
                            System.lineSeparator() + "Instance: " + obj));

            T copy;
            constructor.setAccessible(true);
            try {
                copy = constructor.newInstance(obj);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new Entity.InternalException("Unable to create copy of instance. " +
                        System.lineSeparator() + "Instance: " + obj);
            }

            return copy;
        }

        Constructor<T> constructor;
        constructor = (Constructor<T>) Arrays.stream(obj.getClass().getDeclaredConstructors())
                .filter(c -> c.getGenericParameterTypes().length == 0)
                .findAny()
                .orElseThrow(() -> new Entity.InternalException("Unable to create copy of instance. " +
                        System.lineSeparator() + "Instance: " + obj));

        T copy;
        try {
            constructor.setAccessible(true);
            copy = constructor.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                f.set(copy, getCopyOf(f.get(obj)));
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new Entity.InternalException("Unable to create copy of instance. " + System.lineSeparator() +
                    "Instance: " + obj, e);
        }

        return copy;
    }




    public static boolean isNot(boolean statement) {
        return !statement;
    }
}
