package top.wuare.http.conn;

import java.io.OutputStream;
import java.net.Socket;

/**
 * http response
 * @author wuare
 * @date 2021/6/21
 */
public class HttpResponse {

    private Socket socket;
    private OutputStream out;
    private HttpMessage httpMessage;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public OutputStream getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public HttpMessage getHttpMessage() {
        return httpMessage;
    }

    public void setHttpMessage(HttpMessage httpMessage) {
        this.httpMessage = httpMessage;
    }
}
