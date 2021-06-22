## Http Server
Usage: 
```java
import top.wuare.http.HttpServer;
/**
 * bootstrap
 */
public class HttpServerTest {

    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8082);
        httpServer.addHandler((req, res) -> {
            res.setBody("Hello HttpServer");
            res.flush();
        }).setErrorHandler((req, res, e) -> {
            res.setBody("HttpServer Error");
            res.flush();
        }).start();
    }
}
```