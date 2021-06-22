package top.wuare.http.proto;

import java.util.ArrayList;
import java.util.List;

/**
 * http message
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpMessage {

    private HttpLine httpLine;
    private List<HttpHeader> headers = new ArrayList<>();
    private HttpBody body = new HttpBody();

    {
        headers.add(new HttpHeader("Content-Type", "text/html; charset=utf-8"));
        headers.add(new HttpHeader("Server", "WaSer"));
    }

    public HttpMessage() {
    }

    public HttpMessage(HttpLine httpLine, List<HttpHeader> headers, HttpBody body) {
        this.httpLine = httpLine;
        this.headers = headers;
        this.body = body;
    }

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
