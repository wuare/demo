## Http Server
Usage: 
```java
import top.wuare.http.HttpServer;
/**
 * bootstrap
 */
public class HttpServerTest {
    
    // user default handler
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8082);
        
        // don't set url if you just access static resources
        httpServer.getDefaultRequestHandler().get("/a", (req, res) -> {
            res.setBody("the url is /a");
        }).get("/b", (req, res) -> {
            res.setBody("the url is /b");
        });
        httpServer.start();
    }
    
    // access static resource
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8082);
        // set absolute path, if not, will find static resources in classes directory
        // httpServer.setStaticResourcePathAbsolute(true)
        //     .setStaticResourcePath("C:/");
        httpServer.start();
    }

}
```

## TODO
- [x] parse request headers
- [x] parse request body
- [x] url mapping handle
- [x] static resource handle
