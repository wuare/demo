package top.wuare.http.proto;

import java.nio.charset.StandardCharsets;

/**
 * http message body
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpBody {

    private byte[] data = new byte[0];

    public HttpBody() {
    }

    public HttpBody(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HttpBody{" +
                "data=" + new String(data, StandardCharsets.UTF_8) +
                '}';
    }
}
