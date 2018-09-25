package com.github.paniclab.contentpack;

import java.util.function.Function;

public interface Content<T> {
    static <U> Content<U> valueOf(U attachment) {
        return new DefaultContentImpl<>(attachment);
    }

    static <U, R> R from(Content<U> content, Function<? super U, ? extends R> converter) {
        return content.extractBy(converter);
    }

    static <U, R> R fromConvertibleValue(Content<U> content, Class<? extends R> clazz) {
        return content.extractByConvertibleValueAs(clazz);
    }


    T value();

    default <R> R extractBy(Function<? super T, ? extends R> converter) {
        return converter.apply(value());
    }

    default <R> R extractByConvertibleValueAs(Class<? extends R> clazz) {
        if(this.value() instanceof Convertible) {
            return Convertible.class.cast(value()).convertTo(clazz);
        }
        throw new IllegalArgumentException("Instance of " + this.getClass().getCanonicalName() +
                " can not be converted to " + clazz.getCanonicalName());
    }
}
