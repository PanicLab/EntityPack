package com.github.paniclab.contentpack;

class DefaultContentImpl<T> implements Content<T>{
    private T value;

    DefaultContentImpl(T value) {
        this.value = value;
    }

    @Override
    public T value() {
        return value;
    }
}
