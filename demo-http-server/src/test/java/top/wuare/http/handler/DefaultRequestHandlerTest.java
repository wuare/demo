package top.wuare.http.handler;

import org.junit.Test;
import top.wuare.http.HttpServer;
import top.wuare.http.proto.HttpRequestLine;

/**
 * default request handler test case
 *
 * @author wuare
 * @date 2021/6/24
 */
public class DefaultRequestHandlerTest {

    @Test
    public void testHandle() {
        HttpServer httpServer = new HttpServer(8082);
        DefaultRequestHandler handler = new DefaultRequestHandler(httpServer);
        handler.get("/a", (req, res) ->
                res.setBody("this url is: " + ((HttpRequestLine) (req.getHttpMessage().getHttpLine())).getUrl()));
        httpServer.addHandler(handler);
        // WARN: do not call start method!!!
        // use thread
        Thread t = new Thread(httpServer::start);
        t.start();
        httpServer.setRunning(false);
    }
}
