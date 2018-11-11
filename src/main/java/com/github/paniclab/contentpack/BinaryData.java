package com.github.paniclab.contentpack;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BinaryData implements Convertible {
    private ByteBuffer buffer;

    BinaryData(byte[] bytes) {
        this.buffer = ByteBuffer.wrap(bytes);
    }

    BinaryData(InputStream inputStream) {
        try {
            this.buffer = ByteBuffer.wrap(toBytes(inputStream));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read data from InputStream.", e);
        }
    }

    private byte[] toBytes(InputStream inputStream) throws IOException {
        int count = 0;
        byte[] bytes = new byte[64];

        int b = inputStream.read();
        while (b != -1 ) {
            if(count > bytes.length) {
                bytes = grow(bytes);
            }
            bytes[count++] = (byte) b;

            b = inputStream.read();
        }

        return Arrays.copyOf(bytes, bytes.length);
    }

    private byte[] grow(byte[] bytes) {
        int newCapacity = bytes.length << 1;
        if(newCapacity - bytes.length < 0) {
            throw new OutOfMemoryError();
        }
        return Arrays.copyOf(bytes, newCapacity);
    }

    BinaryData(OutputStream outputStream) {
        try {
            PipedOutputStream pipedOutputStream = new PipedOutputStream(outputStream);
            PipedInputStream pipedInputStream = new PipedInputStream(outputStream);
            this.buffer = ByteBuffer.wrap(toBytes(pipedInputStream));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read data from InputStream.", e);
        }
    }

    private byte[] toBytes(OutputStream outputStream) {
        int count = 0;
        byte[] bytes = new byte[64];

        outputStream
    }
}
