package com.github.paniclab.domain;

import java.io.Serializable;


public interface Entity<T extends Serializable> {

    @SuppressWarnings("unchecked")
    static <ID extends Serializable, T extends Entity<ID>> T getDetached(ID id, Class<T> clazz) {
        return Entities.createDetached(id, clazz);
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

    default boolean equalsByContenExceptId(Entity<T> another) {
        return Entities.equalsByContentExceptId(getThis(), another);
    }

    default boolean equalsByContent(Entity<T> another) {
        return Entities.equalsByContent(getThis(), another);
    }

    default boolean equalsById(Entity<T> another) {
        return Entities.equalsById(getThis(), another);
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
