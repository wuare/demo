package top.wuare.http.conn;

import java.util.List;

/**
 * http message
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpMessage {

    private HttpLine httpLine;
    private List<HttpHeader> headers;
    private HttpBody body;

    public HttpLine getHttpLine() {
        return httpLine;
    }

    public void setHttpLine(HttpLine httpLine) {
        this.httpLine = httpLine;
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<HttpHeader> headers) {
        this.headers = headers;
    }

    public HttpBody getBody() {
        return body;
    }

    public void setBody(HttpBody body) {
        this.body = body;
    }
}
