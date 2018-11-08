package com.github.paniclab.contentpack;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Properties;


public class ContentOutputStream extends OutputStream implements Content<ContentOutputStream>, Convertible {
    private byte[] buffer;
    private int count;
    private Properties properties;

    public ContentOutputStream() {
        super();
        this.buffer = new byte[32];
        this.count = 0;
    }


    @Override
    public ContentOutputStream value() {
        return this;
    }

    @Override
    public void write(int b) {
        ensureCapacity(count + 1);
        buffer[count] = (byte) b;
        count += 1;
    }

    @Override
    public void write(byte[] b) {
        write(b, 0, b.length);
    }

    @Override
    public void write(byte[] b, int off, int len) {
        if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) - b.length > 0)) {
            throw new IndexOutOfBoundsException();
        }

        ensureCapacity(count + len);
        System.arraycopy(b, off, buffer, count, len);
        count += len;
    }

    public void writeTo(OutputStream out) {
        try {
            out.write(buffer, 0, count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] toByteArray() {
        return Arrays.copyOf(buffer, count);
    }

    public int size() {
        return count;
    }

    public Properties getProperties() {
        if(this.properties == null) {
            this.properties = new Properties();
        }
        return properties;
    }

    @Override
    public String toString() {
        return new String(buffer, 0, count);
    }

    public String toString(String charsetName) throws UnsupportedEncodingException {
        return new String(buffer, 0, count, charsetName);
    }


    private void ensureCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - buffer.length > 0)
            grow(minCapacity);
    }

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = buffer.length;
        int newCapacity = oldCapacity << 1;
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
        buffer = Arrays.copyOf(buffer, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }
}
