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
}
```
## 配置文件
服务启动时，在类路径下查找waser.properties配置文件，[示例](https://github.com/wuare/demo/blob/master/demo-http-server/src/main/resources/waser-sample.properties)  
`static.resource.path.type`配置静态资源访问路径类型，可配置值有`relative`、`absolute`、`classpath`三个，默认值为`classpath`，及从类路径下查找静态资源  
`static.resource.path`配置静态资源路径，当type为`relative`、`absolute`时，该值有效  
`static.resource.path.type`各个值所表示的意思
- `relative` 相对路径，表示当前目录在的文件夹下拼接上path值得路径，当部署为jar时，当前目录为jar包所在的目录，本地启动时，当前目录为classes目录，例：当前jar包部署在`/a`目录下，path的配置值为`b/`，则加载静态资源时在`/a/b/`目录下查找
- `absolute` 绝对路径，加载静态资源时直接在path配置的路径下查找
- `classpath` 类路径，加载静态资源时在类路径下查找

## TODO
- [x] parse request headers
- [x] parse request body
- [x] url mapping handle
- [x] static resource handle
- [x] Connection header handle

## Note
1.如果遇到`Connection: keep-alive`请求头，执行完业务逻辑后，需要将输入流中未读取的数据消费完，然后把socket放到线程池中继续处理。  
2.读取body数据的时候，如果content-length太大，使用InputStream.read(new byte[contentLength])这种方式，可能会导致数据读取不完整。
有可能是由于TCP接收缓冲区只有一部分数据，调用完read方法后还没有判断返回值（读取的字节数）有没有等于content-length，所以导致该问题。

