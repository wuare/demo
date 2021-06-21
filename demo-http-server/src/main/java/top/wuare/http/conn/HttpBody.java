package top.wuare.http.conn;

/**
 * http message body
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpBody {

    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
