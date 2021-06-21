package top.wuare.http.conn;

import java.io.InputStream;
import java.net.Socket;

/**
 * http request
 *
 * @author wuare
 * @date 2021/6/21
 */
public class Request {

    private Socket socket;
    private InputStream in;

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
}
