package com.github.paniclab.contentpack;

class DefaultContentWrapper<T> implements Content<T>{
    private T value;

    DefaultContentWrapper(T value) {
        this.value = value;
    }

    @Override
    public T value() {
        return value;
    }
}
