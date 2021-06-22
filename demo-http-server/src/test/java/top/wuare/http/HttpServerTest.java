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
        });
        // WARN: do not call start method!!!
        // use thread
        Thread t = new Thread(httpServer::start);
        t.start();
        httpServer.setRunning(false);
    }

    public void testHandleJson() {
        HttpServer httpServer = new HttpServer(8082);
        httpServer.addHandler((req, res) -> {
            //String header = req.getHeader("Content-Type");
            //if ("application/json".equals(header)) {
                //JsonParser parser = new JsonParser();
                //String body = req.getBody();
                //Object parse = parser.parse(body);
                //System.out.println(parse);
            //}
            res.setBody("Hello HttpServer");
            res.flush();
        }).start();
    }
}
