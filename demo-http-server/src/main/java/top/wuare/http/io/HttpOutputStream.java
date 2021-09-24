package top.wuare.http.io;

import top.wuare.http.proto.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

public class HttpOutputStream extends OutputStream {

    private final HttpResponse response;
    private final OutputStream out;
    public HttpOutputStream(HttpResponse response, OutputStream out) {
        this.response = response;
        this.out = out;
    }

    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
}
