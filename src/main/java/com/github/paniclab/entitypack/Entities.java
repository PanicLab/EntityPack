package com.github.paniclab.entitypack;


import java.io.Serializable;
import java.lang.reflect.*;
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
}
