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

    public static <ID extends Serializable, T extends Entity<ID>> boolean equalsByContentExceptId(T one, T another) {
        boolean isContentExceptIdsMatched;

        try {

            isContentExceptIdsMatched = tryToMatchByContentExceptIds(one, another);

        } catch (IllegalAccessException e) {
            throw new Entity.InternalException("Unable to compare two instances by content." + System.lineSeparator() +
                    "Instance: " + one +
                    System.lineSeparator() + "Another instance: " + another, e);
        }

        return isContentExceptIdsMatched;
    }

    private static <T> boolean tryToMatchByContentExceptIds(T one, T another) throws IllegalAccessException {
        int numberOfFields = one.getClass().getDeclaredFields().length;
        //if (another.getClass().getDeclaredFields().length != numberOfFields) return false;

        Field[] fields = one.getClass().getDeclaredFields();
        for (int x = 0; x < numberOfFields; x++) {
            Field currentField = fields[x];
            currentField.setAccessible(true);

            if (isId(currentField)) {
                continue;
            }
            if (currentField.get(one) == null) {
                if (currentField.get(another) != null) {
                    return false;
                }
                continue;
            }
            if (currentField.getType().isArray()) {
                if (isNot(Arrays.deepEquals((Object[]) currentField.get(one), (Object[]) currentField.get(another)))) {
                    return false;
                }
                continue;
            }
            if (currentField.get(one).getClass().equals(ArrayDeque.class)) {
                if (isNot(Arrays.deepEquals(
                        ArrayDeque.class.cast(currentField.get(one)).toArray(),
                        ArrayDeque.class.cast(currentField.get(another)).toArray()))) {
                    return false;
                }
                continue;
            }
            if (isNot(Objects.equals(currentField.get(one), currentField.get(another)))) {
                return false;
            }
        }

        return true;
    }

    //TODO проверить на наличие аннотаций
    private static boolean isId(Field field) {
        return field.getName().equalsIgnoreCase("id");
    }

    private static Optional<Field> findIdField(Object obj) {
        return Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(Entities::isId)
                .findFirst();
    }

    private static String getIdFieldName(Object obj) {
        return findIdField(obj).map(Field::getName).orElse("");
    }


    public static <ID extends Serializable, T extends Entity<ID>> boolean equalsByContent(T one, T another) {
        boolean isContentExceptIdsMatched;

        try {

            isContentExceptIdsMatched = tryToMatchByContentExceptIds(one, another);

        } catch (IllegalAccessException e) {
            throw new Entity.InternalException("Unable to compare two instances by content." + System.lineSeparator() +
                    "Instance: " + one +
                    System.lineSeparator() + "Another instance: " + another, e);
        }

        return isContentExceptIdsMatched && equalsByIds(one, another);
    }


    public static <ID extends Serializable, T extends Entity<ID>> boolean equalsByIds(T one, T another) {
        return one.getId().equals(another.getId());
    }


    public static boolean isNot(boolean statement) {
        return !statement;
    }

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
            //TODO сначала попробовать найти аннотированные поля @Id и т.д.
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |NoSuchFieldException e) {
            throw new Entity.InternalException("Unable to create instance of class " + clazz.getCanonicalName(), e);
        }

        return entity;
    }


    public static <T> T getCopy(T obj) {
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
        if(     obj instanceof Long ||
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

        if(     Map.class.isAssignableFrom(obj.getClass())) {
                    return SortOfObjectToCopy.IS_CONVENTIONAL_MAP;
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
        Constructor<T>[] constructors = (Constructor<T>[]) obj.getClass().getDeclaredConstructors();// для дебага, убрать
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
        Constructor<T>[] constructors = (Constructor<T>[]) obj.getClass().getDeclaredConstructors();// для дебага, убрать
        Constructor<T> constructor = (Constructor<T>) Arrays.stream(obj.getClass().getDeclaredConstructors())
                    .filter(c -> c.getGenericParameterTypes().length == 1)
                    .filter(c -> isNot(c.getGenericParameterTypes()[0].equals(int.class)))
                    .filter(c -> ((ParameterizedType) c.getGenericParameterTypes()[0]).getRawType().equals(Map.class))
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
                f.set(copy, getCopy(f.get(obj)));
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
        MEDIATE,
        DEEP,
    }
}
