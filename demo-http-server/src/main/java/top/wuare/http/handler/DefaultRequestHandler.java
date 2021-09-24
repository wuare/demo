package top.wuare.http.handler;

import top.wuare.http.HttpServer;
import top.wuare.http.define.Constant;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.handler.request.MethodOptionsRequestHandler;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpRequestLine;
import top.wuare.http.proto.HttpResponse;
import top.wuare.http.util.HttpUtil;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * default request handler
 *
 * @author wuare
 * @since 2021/6/24
 */
public class DefaultRequestHandler implements RequestHandler {

    private static final Logger logger = Logger.getLogger(DefaultHandler.class.getName());

    private final HttpServer httpServer;

    public DefaultRequestHandler(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    private final Map<String, RequestHandler> requestHandlerGetMap = new HashMap<>();
    private final Map<String, RequestHandler> requestHandlerPostMap = new HashMap<>();

    private static final Map<String, String> FILE_CONTENT_TYPE_MAP = new HashMap<>();
    static {
        FILE_CONTENT_TYPE_MAP.put(".png", "image/png");
        FILE_CONTENT_TYPE_MAP.put(".jpg", "image/jpeg");
        FILE_CONTENT_TYPE_MAP.put(".pdf", "application/pdf");
        FILE_CONTENT_TYPE_MAP.put(".ico", "image/x-icon");
        FILE_CONTENT_TYPE_MAP.put(".css", "text/css");
        FILE_CONTENT_TYPE_MAP.put(".dtd", "text/xml;charset=utf-8");
        FILE_CONTENT_TYPE_MAP.put(".htm", "text/html;charset=utf-8");
        FILE_CONTENT_TYPE_MAP.put(".html", "text/html;charset=utf-8");
        FILE_CONTENT_TYPE_MAP.put(".js", "application/x-javascript");
        FILE_CONTENT_TYPE_MAP.put(".mp3", "audio/mp3");
        FILE_CONTENT_TYPE_MAP.put(".mp4", "video/mpeg4");
        FILE_CONTENT_TYPE_MAP.put(".svg", "image/svg+xml");
        FILE_CONTENT_TYPE_MAP.put(".xml", "text/xml;charset=utf-8");
        FILE_CONTENT_TYPE_MAP.put(".txt", "text/plain;charset=utf-8");
    }

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        HttpRequestLine httpLine = (HttpRequestLine) request.getHttpMessage().getHttpLine();
        RequestHandler handler;
        if ("GET".equalsIgnoreCase(httpLine.getMethod())) {
            handler = requestHandlerGetMap.get(HttpUtil.getUrlWithOutQueryParam(httpLine.getUrl()));
        } else if ("POST".equalsIgnoreCase(httpLine.getMethod())) {
            handler = requestHandlerPostMap.get(HttpUtil.getUrlWithOutQueryParam(httpLine.getUrl()));
        } else if ("OPTIONS".equalsIgnoreCase(httpLine.getMethod())) {
            handler = new MethodOptionsRequestHandler();
        } else {
            handlerError(response, HttpStatus.METHOD_NOT_ALLOWED);
            return;
        }
        if (handler == null) {
            // find static resources
            if (handleStaticResource(request, response)) {
                return;
            }
            handleNotFoundError(request, response);
            return;
        }
        handler.handle(request, response);
    }

    private void handleNotFoundError(HttpRequest request, HttpResponse response) {
        RequestHandler notFoundRequestHandler = httpServer.getNotFoundRequestHandler();
        if (notFoundRequestHandler != null) {
            notFoundRequestHandler.handle(request, response);
        }
    }

    private boolean handleStaticResource(HttpRequest request, HttpResponse response) {
        HttpRequestLine httpLine = (HttpRequestLine) request.getHttpMessage().getHttpLine();
        String url = HttpUtil.getUrlWithOutQueryParam(httpLine.getUrl()).substring(1);
        if ("".equals(url)) {
            return false;
        }
        if (url.endsWith(".class")) {
            return false;
        }
        String pathType = httpServer.getProperties().getProperty(Constant.CONFIG_STATIC_RESOURCE_PATH_TYPE,
                Constant.CONFIG_STATIC_RESOURCE_PATH_TYPE_CLASSPATH);
        String path = getStaticResourcePath();
        if (path == null) {
            return false;
        }
        InputStream in = null;
        try {
            if (Constant.CONFIG_STATIC_RESOURCE_PATH_TYPE_ABSOLUTE.equals(pathType)) {
                File file = new File(path + url);
                if (!file.exists()) {
                    return false;
                }
                in = new BufferedInputStream(new FileInputStream(file));
            } else if (Constant.CONFIG_STATIC_RESOURCE_PATH_TYPE_RELATIVE.equals(pathType)) {
                URL location = this.getClass().getProtectionDomain().getCodeSource().getLocation();
                String curPath = URLDecoder.decode(location.getPath(), "UTF-8");
                if (curPath.charAt(curPath.length() - 1) == '/') {
                    curPath = curPath.substring(0, curPath.charAt(curPath.length() - 1));
                }
                if (curPath.endsWith(".jar")) {
                    curPath = curPath.substring(0, curPath.lastIndexOf("/") + 1);
                }
                File file = new File(curPath + path + url);
                if (!file.exists()) {
                    return false;
                }
                in = new BufferedInputStream(new FileInputStream(file));
            } else if (Constant.CONFIG_STATIC_RESOURCE_PATH_TYPE_CLASSPATH.equals(pathType)) {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(url);
            }
            if (in == null) {
                return false;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b = new byte[8192];
            int c;
            while ((c = in.read(b)) != -1) {
                out.write(b, 0, c);
            }
            setResponseContentType(url, response);
            response.setBody(out.toByteArray());
            response.flush();
            return true;
        } catch (IOException e) {
            logger.severe("DefaultRequestHandler#handleStaticResource " + e.getMessage());
            return false;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private String getStaticResourcePath() {
        String path = httpServer.getProperties().getProperty(Constant.CONFIG_STATIC_RESOURCE_PATH);
        if (path != null) {
            char c = path.charAt(path.length() - 1);
            if (c != '/') {
                path = path + "/";
            }
        }
        return path;
    }

    private void setResponseContentType(String url, HttpResponse response) {
        int i = url.lastIndexOf(".");
        if (i < 0) {
            return;
        }
        String suffix = url.substring(i);
        String type = FILE_CONTENT_TYPE_MAP.get(suffix);
        if (type != null) {
            response.addHeader(Constant.HTTP_HEADER_CONTENT_TYPE, type);
        }
    }

    public DefaultRequestHandler get(String path, RequestHandler requestHandler) {
        requestHandlerGetMap.put(path, requestHandler);
        return this;
    }

    public DefaultRequestHandler post(String path, RequestHandler requestHandler) {
        requestHandlerPostMap.put(path, requestHandler);
        return this;
    }

    public static void handlerError(HttpResponse response, HttpStatus httpStatus) {
        if (response == null) {
            return;
        }
        response.setStatus(httpStatus);
        response.setBody(httpStatus.getReasonPhrase());
        response.flush();
    }
}
