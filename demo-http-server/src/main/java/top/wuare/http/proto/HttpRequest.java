package top.wuare.http.proto;

import java.io.InputStream;
import java.net.Socket;

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
}
