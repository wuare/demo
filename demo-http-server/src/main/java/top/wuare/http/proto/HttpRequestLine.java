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
    private String queryParam;

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

    public String getQueryParam() {
        return queryParam;
    }

    public void setQueryParam(String queryParam) {
        this.queryParam = queryParam;
    }

    @Override
    public String toString() {
        return "HttpRequestLine{" +
                "method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", version='" + version + '\'' +
                ", queryParam='" + queryParam + '\'' +
                '}';
    }
}
