package com.github.paniclab.entitypack;

public interface Instantiable {
    default Object getThis() {
        return this;
    }

    default boolean isNot(boolean statement) {
        return !statement;
    }
}
