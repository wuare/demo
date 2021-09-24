package top.wuare.http.io;

import java.io.IOException;
import java.io.OutputStream;

public class HttpOutputStream extends OutputStream {

    private final OutputStream out;

    public HttpOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
}
