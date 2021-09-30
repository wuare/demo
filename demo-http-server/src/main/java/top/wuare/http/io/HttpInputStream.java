package top.wuare.http.io;

import java.io.IOException;
import java.io.InputStream;

public class HttpInputStream extends InputStream {

    private final InputStream in;
    private int remainLength;
    private final int length;

    public HttpInputStream(InputStream in, int contentLength) {
        this.in = in;
        this.remainLength = contentLength;
        this.length = contentLength;
    }

    @Override
    public int read() throws IOException {
        if (remainLength-- > 0) {
            return in.read();
        }
        return -1;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (remainLength > 0) {
            int nRead = in.read(b, off, len);
            if (nRead > 0) {
                remainLength = remainLength - nRead;
                return nRead;
            }
        }
        return -1;
    }

    @Override
    public void close() throws IOException {
        in.close();
    }

    public int getLength() {
        return length;
    }

    public int getRemainLength() {
        return remainLength;
    }
}
