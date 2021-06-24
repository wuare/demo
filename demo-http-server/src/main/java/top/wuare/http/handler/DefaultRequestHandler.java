package top.wuare.http.handler;

import top.wuare.http.define.HttpStatus;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpRequestLine;
import top.wuare.http.proto.HttpResponse;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @Override
    public void handle(HttpRequest request, HttpResponse response) {
        HttpRequestLine httpLine = (HttpRequestLine) request.getHttpMessage().getHttpLine();
        RequestHandler handler;
        if ("GET".equals(httpLine.getMethod().toUpperCase())) {
            handler = requestHandlerGetMap.get(httpLine.getUrl());
        } else if ("POST".equals(httpLine.getMethod().toUpperCase())) {
            handler = requestHandlerPostMap.get(httpLine.getUrl());
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
        String url = httpLine.getUrl().substring(1);
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
            response.setBody(out.toByteArray());
            response.flush();
            return true;
        } catch (IOException e) {
            logger.severe(e.getMessage());
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
