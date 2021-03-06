package top.wuare.http.handler;

import top.wuare.http.HttpServer;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.exception.HttpReadTimeOutException;
import top.wuare.http.exception.HttpRequestClosedException;
import top.wuare.http.parser.HttpMessageParser;
import top.wuare.http.proto.HttpRequest;
import top.wuare.http.proto.HttpResponse;
import top.wuare.http.util.IOUtil;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * default handler for handle http request
 *
 * @author wuare
 * @since 2021/6/21
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
            response = buildResponse(socket);
            response.setStatus(HttpStatus.OK);
            List<RequestHandler> requestHandlers = httpServer.getRequestHandlers();
            if (requestHandlers.isEmpty()) {
                httpServer.getDefaultRequestHandler().handle(request, response);
            } else {
                for (RequestHandler handler : requestHandlers) {
                    handler.handle(request, response);
                }
            }
            response.getResponseHelper().flush();
        } catch (HttpReadTimeOutException | HttpRequestClosedException | IOException e) {
            IOUtil.close(socket);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            handleError(request, response, e);
        } finally {
            // consume data at this request
            if (request != null) {
                endRequest(request);
            }
            handleKeepAlive(request);
        }
    }

    private HttpResponse buildResponse(Socket socket) throws IOException {
        return new HttpResponse(socket);
    }

    private void endRequest(HttpRequest request) {
        if (request == null || request.getInputStream() == null) {
            return;
        }
        if (request.getSocket() == null || request.getSocket().isClosed()) {
            return;
        }
        byte[] buf = new byte[2048];
        try {
            while (true) {
                if (request.getInputStream().read(buf) == -1) {
                    break;
                }
            }
        } catch (IOException e) {
            logger.severe("DefaultHandler#endRequest " + e.getMessage());
        }
    }

    private void handleKeepAlive(HttpRequest request) {
        if (request == null) {
            return;
        }
        try {
            ThreadPoolExecutor executor = httpServer.getExecutor();
            int activeCount = executor.getActiveCount();
            int corePoolSize = executor.getCorePoolSize();
            if (socket != null && socket.isConnected() && !socket.isClosed()
                    && "keep-alive".equals(request.getHeader("Connection"))
                    && activeCount < corePoolSize - 5) {
                executor.execute(new DefaultHandler(httpServer, socket));
            } else {
                IOUtil.close(socket);
            }
        } catch (Exception e) {
            logger.severe("DefaultHandler#handleKeepAlive" + e.getMessage());
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
        if (errorHandler == null) {
            return;
        }
        errorHandler.handle(request, response, e);
        try {
            response.getResponseHelper().flush();
        } catch (IOException ex) {
            IOUtil.close(socket);
        }
    }
}
