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
        });
        // WARN: do not call start method!!!
        // use thread
        Thread t = new Thread(httpServer::start);
        t.start();
        httpServer.setRunning(false);
    }

    @Test
    public void testStart0() {
        HttpServer httpServer = new HttpServer(8083);
        httpServer.get("/a", (req, res) -> res.setBody("method is GET, the path is /a"))
                .post("/a", (req, res) -> res.setBody("method is POST, the path is /a"))
        ;
        // WARN: do not call start method!!!
        // use thread
        Thread t = new Thread(httpServer::start);
        t.start();
        httpServer.setRunning(false);
    }

    public void testStart1() {
        HttpServer httpServer = new HttpServer(80);
        httpServer.get("/a", (req, res) -> {
                    res.setBody("method is GET, the path is /a");
                })
                .post("/a", (req, res) -> {
                    res.setBody("method is POST, the path is /a");
                })
        ;
        httpServer.start();
    }
}
