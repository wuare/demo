package top.wuare.http.handler;

import top.wuare.http.HttpServer;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.parser.HttpMessageParser;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.http.util.IOUtil;

import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.logging.Logger;

/**
 * default handler for handle http request
 *
 * @author wuare
 * @date 2021/6/21
 */
public class DefaultHandler implements Runnable {

    private static final Logger logger = Logger.getLogger(DefaultHandler.class.getName());
    private final Socket socket;
    private final HttpServer httpServer;
    private final HttpMessageParser parser = new HttpMessageParser();

    public DefaultHandler(HttpServer httpServer, Socket socket) {
        this.httpServer = httpServer;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket.isClosed()) {
            return;
        }
        HttpRequest request = null;
        HttpResponse response = null;
        try {
            request = parser.parseRequest(socket.getInputStream());
            request.setSocket(socket);
            request.addHeader("staticAbsolute", String.valueOf(httpServer.isStaticResourcePathAbsolute()));
            request.addHeader("staticPath", String.valueOf(httpServer.getStaticResourcePath()));
            response = new HttpResponse(socket, socket.getOutputStream());
            response.setStatus(HttpStatus.OK);
            List<RequestHandler> requestHandlers = httpServer.getRequestHandlers();
            if (requestHandlers.isEmpty()) {
                httpServer.getDefaultRequestHandler().handle(request, response);
                handleKeepAlive(request);
                return;
            }
            for (RequestHandler handler : requestHandlers) {
                handler.handle(request, response);
            }
        } catch (SocketTimeoutException e) {
            logger.warning("the socket timeout " + socket);
            IOUtil.close(socket);
        } catch (Exception e) {
            logger.severe(e.getMessage());
            handleError(request, response, e);
        } finally {
            if (response != null && !response.isFlushed()) {
                response.flush();
            }
        }
        handleKeepAlive(request);
    }

    private void handleKeepAlive(HttpRequest request) {
        if (request == null) {
            return;
        }
        try {
            if (socket != null && socket.isConnected() && !socket.isClosed() && "keep-alive".equals(request.getHeader("Connection"))) {
                httpServer.getExecutorService().execute(new DefaultHandler(httpServer, socket));
            } else {
                IOUtil.close(socket);
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
            IOUtil.close(socket);
        }
    }

    private void handleError(HttpRequest request, HttpResponse response, Exception e) {
        if (response == null) {
            return;
        }
        if (socket.isClosed()) {
            return;
        }
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        RequestErrorHandler errorHandler = httpServer.getErrorHandler();
        // TODO default error handler
        if (errorHandler == null) {
            return;
        }
        errorHandler.handle(request, response, e);
    }
}
