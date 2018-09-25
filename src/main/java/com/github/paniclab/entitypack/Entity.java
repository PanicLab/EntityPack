package com.github.paniclab.entitypack;

import java.io.Serializable;

public interface Entity<ID extends Serializable> extends Instantiable {
    @Override
    default Entity<ID> getThis() {
        return this;
    }

    ID getId();

    default boolean isTransient() {
        return !isAlreadyPersisted();
    }

    default boolean isAlreadyPersisted() {
        return getId() != null;
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
