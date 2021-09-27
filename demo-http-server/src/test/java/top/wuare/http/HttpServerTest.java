package top.wuare.http;

import org.junit.Test;
import top.wuare.http.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

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
        httpServer.addHandler((req, res) -> res.setBody("Hello HttpServer"));
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
                    // 文件下载 使用res.getOutputStream() write()方法
                    // 如果调用了write()方法，setBody()方法将不起作用
                    res.setContentType("application/octet-stream");
                    res.addHeader("Content-Disposition", "attachment;filename=xxx");
                    File file = new File("xxx");
                    OutputStream outputStream = res.getOutputStream();
                    InputStream in = null;
                    try {
                        in = new FileInputStream(file);
                        byte[] buf = new byte[4096];
                        int c;
                        while ((c = in.read(buf)) != -1) {
                            outputStream.write(buf, 0, c);
                        }
                    } catch (Exception e) {
                        IOUtil.close(in);
                    }
                })
                .post("/a", (req, res) -> res.setBody("method is POST, the path is /a"))
        ;
        httpServer.start();
    }
}
