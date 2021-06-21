package top.wuare.http.handler;

import java.net.Socket;

/**
 * default handler for handle http request
 *
 * @author wuare
 * @date 2021/6/21
 */
public class DefaultHandler implements Runnable {

    private final Socket socket;

    public DefaultHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }

    public Socket getSocket() {
        return socket;
    }
}
