package top.wuare.http.handler;

import top.wuare.http.define.Constant;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.handler.request.MethodOptionsRequestHandler;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpRequestLine;
import top.wuare.http.proto.HttpResponse;
import top.wuare.http.util.HttpUtil;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * default request handler
 *
 * @author wuare
 * @date 2021/6/24
 */
public class DefaultRequestHandler implements RequestHandler {

    private static final Logger logger = Logger.getLogger(DefaultHandler.class.getName());


    private final Map<String, RequestHandler> requestHandlerGetMap = new HashMap<>();
    private final Map<String, RequestHandler> requestHandlerPostMap = new HashMap<>();

    private static final Map<String, String> FILE_CONTENT_TYPE_MAP = new HashMap<>();
    static {
        FILE_CONTENT_TYPE_MAP.put(".png", "image/png");
        FILE_CONTENT_TYPE_MAP.put(".jpg", "image/jpeg");
        FILE_CONTENT_TYPE_MAP.put(".pdf", "application/pdf");
        FILE_CONTENT_TYPE_MAP.put(".ico", "image/x-icon");
        FILE_CONTENT_TYPE_MAP.put(".css", "text/css");
        FILE_CONTENT_TYPE_MAP.put(".dtd", "text/xml");
        FILE_CONTENT_TYPE_MAP.put(".htm", "text/html");
        FILE_CONTENT_TYPE_MAP.put(".html", "text/html");
        FILE_CONTENT_TYPE_MAP.put(".js", "application/x-javascript");
        FILE_CONTENT_TYPE_MAP.put(".mp3", "audio/mp3");
        FILE_CONTENT_TYPE_MAP.put(".mp4", "video/mpeg4");
        FILE_CONTENT_TYPE_MAP.put(".svg", "image/svg+xml");
        FILE_CONTENT_TYPE_MAP.put(".xml", "text/xml");
        FILE_CONTENT_TYPE_MAP.put(".txt", "text/plain");
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
            handlerError(response, HttpStatus.NOT_FOUND);
            return;
        }
        handler.handle(request, response);
        if (!response.isFlushed()) {
            response.flush();
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
        String staticAbsolute = request.getHeader("staticAbsolute");
        String staticPath = request.getHeader("staticPath");
        InputStream in = null;
        try {
            if ("true".equals(staticAbsolute)) {
                if (staticPath == null || "".equals(staticPath)) {
                    return false;
                }
                if (staticPath.charAt(staticPath.length() - 1) != '/') {
                    staticPath = staticPath + "/";
                }
                File file = new File(staticPath + url);
                if (!file.exists()) {
                    return false;
                }
                in = new BufferedInputStream(new FileInputStream(file));
            } else {
                in = Thread.currentThread().getContextClassLoader().getResourceAsStream(url);
                if (in == null) {
                    return false;
                }
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] b = new byte[2048];
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
