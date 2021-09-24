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
    public synchronized int read() throws IOException {
        if (remainLength-- > 0) {
            return in.read();
        }
        return -1;
    }

    @Override
    public synchronized int read(byte[] b) throws IOException {
        if (remainLength > 0) {
            int nRead = in.read(b);
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
