package com.github.paniclab.domain;


import java.io.Serializable;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

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



    public static <ID extends Serializable, T extends Entity<ID>> boolean hasEqualsId(T one, T another) {
        return one.getId().equals(another.getId());
    }


    public static boolean isNot(boolean statement) {
        return !statement;
    }


    public static <T> T getCopyOf(T obj) {
        if (obj == null) return null;

        SortOfObjectToCopy copyCase = detectCopyCase(obj);

        switch (copyCase) {
            case IS_PRIMITIVE_WRAPPER:
                return copyPrimitiveWrapper(obj);
            case IS_STRING:
                return copyString(obj);
            case IS_BIG_NUMBER:
                return copyBigNumber(obj);
            case IS_ENUM:
                return copyEnum(obj);
            case IS_ARRAY:
                return copyArray(obj);
            case IS_ARRAY_AS_LIST:
                return copyArrayAsList(obj);
            case IS_CONVENTIONAL_COLLECTION:
                return copyConventionalCollection(obj);
            case IS_CONVENTIONAL_MAP:
                return copyConventionalMap(obj);
            default:
                return copyPojo(obj);
        }
    }

    private static <T> SortOfObjectToCopy detectCopyCase(T obj) {
        if (    obj instanceof Long ||
                obj instanceof Integer ||
                obj instanceof Short ||
                obj instanceof Byte ||
                obj instanceof Boolean ||
                obj instanceof Character ||
                obj instanceof Float ||
                obj instanceof Double
                ) {
                        return SortOfObjectToCopy.IS_PRIMITIVE_WRAPPER;
        }

        if(     obj instanceof String) {
                        return SortOfObjectToCopy.IS_STRING;
        }

        if(     obj instanceof BigInteger ||
                obj instanceof BigDecimal) {
                        return SortOfObjectToCopy.IS_BIG_NUMBER;
        }

        if(     obj instanceof Enum) {
                        return SortOfObjectToCopy.IS_ENUM;
        }

        if(     obj.getClass().isArray()) {
                        return SortOfObjectToCopy.IS_ARRAY;
        }

        if(     obj.getClass().getCanonicalName().equals("java.util.Arrays.ArrayList")) {
                        return SortOfObjectToCopy.IS_ARRAY_AS_LIST;
        }

        if(     Collection.class.isAssignableFrom(obj.getClass())) {
                        return SortOfObjectToCopy.IS_CONVENTIONAL_COLLECTION;
        }

        return SortOfObjectToCopy.IS_POJO;
    }

    private static <T> T copyPrimitiveWrapper(T obj) {
        return obj;
    }

    private static <T> T copyString(T str) {
        return str;
    }

    private static <T> T copyBigNumber(T number) {
        return number;
    }

    private static <T> T copyEnum(T obj) {
        return obj;
    }

    private static <T> T copyArray(T array) {
        int arraySize = Array.getLength(array);
        Class elementType = array.getClass().getComponentType();

        @SuppressWarnings("unchecked")
        T copy = (T)Array.newInstance(elementType, arraySize);
        for (int x = 0; x < arraySize; x++) {
            Array.set(copy, x, Array.get(array, x));
        }

        return copy;
    }

    @SuppressWarnings("unchecked")
    private static <T> T copyArrayAsList(T array) {
        List list = (List) array;
        int size = list.size();
        Object[] copy = (Object[]) Array.newInstance(Object.class, size);
        for (int x=0; x < size; x++) {
            Array.set(copy, x, list.get(x));
        }

        return (T)Arrays.asList(copy);
    }

    @SuppressWarnings("unchecked")
    private static <T> T copyConventionalCollection(T obj) {
        Constructor<T>[] constructors = (Constructor<T>[]) obj.getClass().getDeclaredConstructors();
        Constructor<T> constructor = (Constructor<T>) Arrays.stream(obj.getClass().getDeclaredConstructors())
                .filter(c -> c.getGenericParameterTypes().length == 1)
                .filter(c -> isNot(c.getGenericParameterTypes()[0].equals(int.class)))
                .filter(c -> ((ParameterizedType) c.getGenericParameterTypes()[0]).getRawType().equals(Collection.class))
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

    @SuppressWarnings("unchecked")
    private static <T> T copyConventionalMap(T obj) {
        Constructor<T> constructor = (Constructor<T>) Arrays.stream(obj.getClass().getDeclaredConstructors())
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

    @SuppressWarnings("unchecked")
    private static <T> T copyPojo(T obj) {

        Constructor<T> constructor = (Constructor<T>) Arrays.stream(obj.getClass().getDeclaredConstructors())
                            .filter(c -> c.getGenericParameterTypes().length == 0)
                            .findAny()
                            .orElseThrow(() -> new Entity.InternalException("Unable to create copy of instance. " +
                                    "Entity must have no-arg constructor." +
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

    enum SortOfObjectToCopy {
        IS_PRIMITIVE_WRAPPER,
        IS_STRING,
        IS_BIG_NUMBER,
        IS_ENUM,
        IS_ARRAY,
        IS_ARRAY_AS_LIST,
        IS_HASHMAP_KEYSET,
        IS_CONVENTIONAL_COLLECTION,
        IS_CONVENTIONAL_MAP,
        IS_POJO,
    }

    enum CopyMode {
        ARBITRARY,
        DEEP,
    }
}
