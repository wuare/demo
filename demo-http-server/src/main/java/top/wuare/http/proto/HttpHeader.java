package top.wuare.http.proto;

/**
 * http message header
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpHeader {

    public String key;
    public String value;

    public HttpHeader() {
    }

    public HttpHeader(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "HttpHeader{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
