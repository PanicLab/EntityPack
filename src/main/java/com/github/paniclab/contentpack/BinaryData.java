package com.github.paniclab.contentpack;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class BinaryData extends OutputStream implements Convertible {
    private ByteBuffer buffer;

    BinaryData(byte[] bytes) {
        this.buffer = ByteBuffer.wrap(bytes);
    }

    BinaryData(InputStream inputStream) {
        try {
            this.buffer = ByteBuffer.wrap(toBytes(inputStream));
        } catch (IOException e) {
            throw new RuntimeException("Cannot read data from InputStream. InputStream: " + inputStream, e);
        }
    }

    private byte[] toBytes(InputStream inputStream) throws IOException {
        int count = 0;
        byte[] bytes = new byte[64];

        int b = inputStream.read();
        while (b != -1 ) {
            if(count > bytes.length) {
                bytes = growBuffer(bytes);
            }
            bytes[count++] = (byte) b;

            b = inputStream.read();
        }

        return Arrays.copyOf(bytes, bytes.length);
    }

    private void growBuffer() {
        int newCapacity = buffer.limit() << 1;
        if(newCapacity - buffer.limit() < 0) {
            throw new OutOfMemoryError();
        }
        int position = this.buffer.position();
        this.buffer = ByteBuffer.wrap(new byte[newCapacity]);
        this.buffer.position(position);
    }


    @Override
    public void write(int b) {
        if (buffer.limit() - buffer.position() == 1) {
            growBuffer();
        }
        buffer.put((byte) b);
    }
}
