package com.github.paniclab.contentpack;

import java.util.function.Function;

public interface Content<T> {
    static <U> Content<U> valueOf(U attachment) {
        return new DefaultContentImpl<>(attachment);
    }

    static <U, R> R from(Content<U> content, Function<U, R> converter) {
        return content.extractBy(converter);
    }

    T value();
    default <R> R extractBy(Function<T, R> converter) {
        return converter.apply(value());
    }
}
