package top.wuare.lang.env;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Console {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    public void write(String text) {
        try {
            buffer.write(text.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() {
        buffer.reset();
    }

    public String toString() {
        try {
            return buffer.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return buffer.toString();
        }
    }
}
