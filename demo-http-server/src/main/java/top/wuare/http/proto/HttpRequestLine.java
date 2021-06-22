package top.wuare.http.proto;

/**
 * request line
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpRequestLine extends HttpLine {

    private String method;
    private String url;
    private String version;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "HttpRequestLine{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}