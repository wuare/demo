package top.wuare.http;

import org.junit.Test;

/**
 * test http server
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpServerTest {

    @Test
    public void testStart() {
        HttpServer httpServer = new HttpServer(8082);
        httpServer.addHandler((req, res) -> {
            res.setBody("Hello HttpServer");
            res.flush();
        }).setErrorHandler((req, res, e) -> {
            res.setBody("HttpServer Error");
            res.flush();
        });
        Thread t = new Thread(httpServer::start);
        t.start();
        httpServer.setRunning(false);
    }
}
