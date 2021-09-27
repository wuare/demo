package top.wuare.http.io;

import top.wuare.http.proto.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class HttpOutputStream extends OutputStream {

    private final HttpResponse response;
    private final OutputStream out;
    private final byte[] buffer = new byte[8192];
    private int count;


    public HttpOutputStream(HttpResponse response, OutputStream out) {
        this.response = response;
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        prepareResponse();
        buffer[count++] = (byte) b;
        if (count == buffer.length) {
            flush();
        }
    }

    @Override
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);

    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        Objects.requireNonNull(b);
        if (len == 0) {
            return;
        }
        for (int i = 0 ; i < len ; i++) {
            write(b[off + i]);
        }
    }

    @Override
    public void flush() throws IOException {
        if (count > 0) {
            String lenText = Integer.toHexString(count) + "\r\n";
            out.write(lenText.getBytes(StandardCharsets.UTF_8));
            out.write(buffer, 0, count);
            out.write("\r\n".getBytes(StandardCharsets.UTF_8));
        }
        reset();
    }

    @Override
    public void close() throws IOException {
    }

    private void reset() {
        count = 0;
    }

    private void prepareResponse() throws IOException {
        response.addHeader("Transfer-Encoding", "chunked");
        response.getResponseHelper().prepareResponse();
    }
}
