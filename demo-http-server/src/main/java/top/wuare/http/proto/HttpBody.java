package top.wuare.http.proto;

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
}
