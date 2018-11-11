package com.github.paniclab.contentpack;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
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
            throw new RuntimeException("Cannot read data from InputStream. InputStream: " + inputStream, e);
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
            File file = File.createTempFile("bdt", null);

            FileChannel channel = new FileOutputStream(file).getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size());
            this.buffer = buffer.load();
            file.delete();
        } catch (IOException e) {
            throw new RuntimeException("Cannot read data from OutputStream. OutputStream: " + outputStream, e);
        }
    }
}
