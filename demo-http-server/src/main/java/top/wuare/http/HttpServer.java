package top.wuare.http;

import top.wuare.http.define.HttpStatus;
import top.wuare.http.exception.HttpServerException;
import top.wuare.http.handler.DefaultHandler;
import top.wuare.http.handler.RequestErrorHandler;
import top.wuare.http.handler.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * http server
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpServer {

    // jdk logger
    private static final Logger logger = Logger.getLogger(HttpServer.class.getName());

    // thread pool config
    private final int coreSize = Runtime.getRuntime().availableProcessors();
    private final int maxSize = coreSize + coreSize / 2;
    private final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(50);
    private final ExecutorService executorService =
            new ThreadPoolExecutor(coreSize, maxSize, 10, TimeUnit.MINUTES, blockingQueue);

    // server socket
    private ServerSocket serverSocket;
    private final int port;

    // start flag
    private volatile boolean started = false;
    // run flag
    private volatile boolean running = true;

    // handler
    private final List<RequestHandler> requestHandlers = new LinkedList<>();

    // static resource path
    private String staticResourcePath;

    public HttpServer(int port) {
        this.port = port;
    }

    private void init() {
        if (started) {
            return;
        }
        started = true;
        if (getPort() <= 0) {
            throw new HttpServerException("the port is not valid");
        }
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new HttpServerException(e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        }));
    }

    public void start() {
        init();
        logger.info("the thread pool config, coreSize: {" + coreSize + "}, maxSize: {" + maxSize + "}.");
        while (isRunning()) {
            try {
                Socket socket = serverSocket.accept();
                executorService.execute(new DefaultHandler(this, socket));
            } catch (IOException e) {
                logger.severe(e.getMessage());
            }
        }
    }

    public HttpServer setStaticResourcePath(String path) {
        staticResourcePath = path;
        return this;
    }

    public String getStaticResourcePath() {
        return staticResourcePath;
    }

    public HttpServer addHandler(RequestHandler handler) {
        requestHandlers.add(handler);
        return this;
    }

    public void removeHandler(RequestHandler handler) {
        requestHandlers.remove(handler);
    }

    public void clearHandler() {
        requestHandlers.clear();
    }

    public int getPort() {
        return port;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public List<RequestHandler> getRequestHandlers() {
        return requestHandlers;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    // error handler
    private RequestErrorHandler errorHandler = (req, res, e) -> {
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        if (e == null) {
            res.setBody(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return;
        }
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder errorBuilder = new StringBuilder();
        errorBuilder.append("<h1>").append(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()).append("</h1><br/>");
        Arrays.stream(stackTrace).forEach(v -> {
            errorBuilder.append(v.toString()).append("<br/>");
        });
        res.setBody(errorBuilder.toString());
        res.flush();
    };

    public HttpServer setErrorHandler(RequestErrorHandler handler) {
        this.errorHandler = handler;
        return this;
    }

    public RequestErrorHandler getErrorHandler() {
        return errorHandler;
    }
}
