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
        httpServer.get("/a", (req, res) -> {
            res.setBody("the url is /a");
        }).get("/b", (req, res) -> {
            res.setBody("the url is /b");
        });
        httpServer.start();
    }
    
    // access static resource
    public static void main(String[] args) {
        HttpServer httpServer = new HttpServer(8082);
        // set absolute path, if not, will find static resources in classpath
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
- [ ] Connection header handle

## Note
1.如果遇到`Connection: keep-alive`请求头，执行完业务逻辑后，需要将输入流中未读取的数据消费完，然后把socket放到线程池中继续处理。  
2.读取body数据的时候，如果content-length太大，使用InputStream.read(new byte[contentLength])这种方式，可能会导致数据读取不完整。
有可能是由于TCP接收缓冲区只有一部分数据，调用完read方法后还没有判断返回值（读取的字节数）有没有等于content-length，所以导致该问题。

