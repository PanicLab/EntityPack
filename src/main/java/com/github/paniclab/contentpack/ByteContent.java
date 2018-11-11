package com.github.paniclab.contentpack;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;

public class ByteContent<T> implements Content<T> {
    private T value;
    private ByteBuffer buffer;

    private ByteContent(T value) {
        this.value = value;
    }

    public static <U> ByteContent from(U value) {
        Class<?> valueClass = value.getClass();

        if(valueClass == byte[].class || valueClass == Byte.class) {
            return getInstance(byte[].class.cast(value));
        }
    }

    private static ByteContent getInstance(byte[] bytes) {
        ByteContent byteContent = new ByteContent<>(bytes);
        byteContent.buffer = ByteBuffer.wrap(bytes);

        return byteContent;
    }

    private static ByteContent getInstance(InputStream inputStream) {

    }

    private static ByteContent getInstance(OutputStream outputStream) {

    }

    private static ByteContent getInstance(Channel channel) {

    }

    @Override
    public T value() {
        return value;
    }
}
