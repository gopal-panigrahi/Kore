package org.xbmc.kore.ui.sections.localfile;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class RandomAccessFileInputStream extends InputStream {
    private final RandomAccessFile raf;
    private long bytesRemaining;
    private final byte[] singleByte = new byte[1];

    public RandomAccessFileInputStream(RandomAccessFile raf, long length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length must be non-negative");
        }
        this.raf = raf;
        this.bytesRemaining = length;
    }

    @Override
    public int read() throws IOException {
        int read = read(singleByte, 0, 1);
        return (read == -1) ? -1 : (singleByte[0] & 0xFF);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (bytesRemaining <= 0) {
            return -1;
        }
        int toRead = (int) Math.min(len, bytesRemaining);
        int bytesRead = raf.read(b, off, toRead);
        if (bytesRead > 0) {
            bytesRemaining -= bytesRead;
        }
        return bytesRead;
    }

    @Override
    public void close() throws IOException {
        raf.close();
    }

    @Override
    public int available() throws IOException {
        return (int) Math.min(bytesRemaining, Integer.MAX_VALUE);
    }
}