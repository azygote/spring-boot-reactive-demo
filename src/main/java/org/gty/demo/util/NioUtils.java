package org.gty.demo.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

public final class NioUtils {

    private NioUtils() {
    }

    public static byte[] toByteArray(InputStream in) {
        try (var input = Channels.newChannel(in);
             var byteArrayOutputStream = new ByteArrayOutputStream();
             var bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream, 8);
             var output = Channels.newChannel(bufferedOutputStream)) {

            var buffer = ByteBuffer.allocate(16);

            while (input.read(buffer) != -1) {
                buffer.flip();

                while (buffer.hasRemaining()) {
                    output.write(buffer);
                }

                buffer.clear();
            }

            bufferedOutputStream.flush();

            return byteArrayOutputStream.toByteArray();

        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
