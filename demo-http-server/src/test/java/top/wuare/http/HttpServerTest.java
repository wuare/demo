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
        HttpServer httpServer = new HttpServer();
        httpServer.start();
    }
}
