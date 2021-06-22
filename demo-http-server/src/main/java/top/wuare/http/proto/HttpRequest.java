package top.wuare.http.proto;

import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * http request
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpRequest {

    private Socket socket;
    private InputStream in;
    private HttpMessage httpMessage;

    public HttpRequest() {
    }

    public HttpRequest(Socket socket, InputStream in) {
        this.socket = socket;
        this.in = in;
    }

    public HttpRequest(Socket socket, InputStream in, HttpMessage httpMessage) {
        this.socket = socket;
        this.in = in;
        this.httpMessage = httpMessage;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public HttpMessage getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(HttpMessage httpMessage) {
        this.httpMessage = httpMessage;
    }

    public String getHeader(String key) {
        return httpMessage.getHeaders().stream()
                .filter(v -> v.getKey().equals(key))
                .findFirst().map(HttpHeader::getValue).orElse(null);
    }

    public String getBody() {
        return new String(httpMessage.getBody().getData(), StandardCharsets.UTF_8);
    }

    public byte[] getOriginalBody() {
        return httpMessage.getBody().getData();
    }
}
