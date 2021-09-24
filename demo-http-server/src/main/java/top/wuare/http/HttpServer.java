package top.wuare.http;

import top.wuare.http.define.Constant;
import top.wuare.http.define.HttpStatus;
import top.wuare.http.exception.HttpServerException;
import top.wuare.http.handler.DefaultHandler;
import top.wuare.http.handler.DefaultRequestHandler;
import top.wuare.http.handler.RequestErrorHandler;
import top.wuare.http.handler.RequestHandler;
import top.wuare.http.handler.request.NotFoundRequestHandler;
import top.wuare.http.util.ExceptionUtil;
import top.wuare.http.util.IOUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * http server
 *
 * @author wuare
 * @since 2021/6/21
 */
public class HttpServer {

    // jdk logger
    private static final Logger logger = Logger.getLogger(HttpServer.class.getName());

    // thread pool config
    private final int coreSize = 25;
    private final int maxSize = coreSize * 2;
    private ThreadPoolExecutor executor;

    // server socket
    private ServerSocket serverSocket;
    private final int port;

    // start flag
    private volatile boolean started = false;
    // run flag
    private volatile boolean running = true;

    // handler
    private final List<RequestHandler> requestHandlers = new LinkedList<>();
    private final DefaultRequestHandler defaultRequestHandler = new DefaultRequestHandler(this);
    private RequestHandler notFoundRequestHandler = new NotFoundRequestHandler();

    // properties
    private Properties properties;

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
        loadProperties();
        initExecutor();
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

    private void initExecutor() {
        String coreSizeStr = properties.getProperty(Constant.CONFIG_THREAD_POOL_CORE_SIZE, coreSize + "");
        String maxSizeStr = properties.getProperty(Constant.CONFIG_THREAD_POOL_MAX_SIZE, maxSize + "");
        String queueSizeStr = properties.getProperty(Constant.CONFIG_THREAD_POOL_QUEUE_SIZE,  "200");
        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue<>(Integer.parseInt(queueSizeStr));
        this.executor = new ThreadPoolExecutor(Integer.parseInt(coreSizeStr),
                Integer.parseInt(maxSizeStr), 10, TimeUnit.MINUTES, blockingQueue);
    }

    private void loadProperties() {
        InputStream in = HttpServer.class.getClassLoader().getResourceAsStream(Constant.CONFIG_FILE_NAME);
        if (in == null) {
            return;
        }
        try {
            logger.info("load properties start");
            Properties properties = new Properties();
            properties.load(in);
            setProperties(properties);

            Set<String> names = properties.stringPropertyNames();
            for (String name : names) {
                logger.info("the properties key: {" + name + "}, value: {" + properties.getProperty(name) + "}");
            }
            logger.info("load properties end");
        } catch (IOException e) {
            logger.severe("HttpServer#loadProperties " + e.getMessage());
        } finally {
            IOUtil.close(in);
        }
    }

    public void start() {
        init();
        logger.info("the thread pool config, coreSize: {" + coreSize + "}, maxSize: {" + maxSize + "}.");
        initAndStartTasks();
        logger.info("server started, listen port at " + port);
        while (isRunning()) {
            try {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(20000);
                executor.execute(new DefaultHandler(this, socket));
            } catch (IOException e) {
                logger.log(Level.SEVERE, "", e);
            }
        }
    }

    private void initAndStartTasks() {

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

    public DefaultRequestHandler getDefaultRequestHandler() {
        return defaultRequestHandler;
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

    public ThreadPoolExecutor getExecutor() {
        return executor;
    }

    // error handler
    private RequestErrorHandler errorHandler = (req, res, e) -> {
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        if (e == null) {
            res.setBody(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            return;
        }
        String errorBuilder = "<h1>" + HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() + "</h1><br/>" +
                ExceptionUtil.getStackTrace(e);
        res.setBody(errorBuilder.replaceAll("\n", "<br/>"));
        res.flush();
    };

    public HttpServer setErrorHandler(RequestErrorHandler handler) {
        this.errorHandler = handler;
        return this;
    }

    public RequestErrorHandler getErrorHandler() {
        return errorHandler;
    }


    public HttpServer get(String path, RequestHandler handler) {
        defaultRequestHandler.get(path, handler);
        return this;
    }

    public HttpServer post(String path, RequestHandler handler) {
        defaultRequestHandler.post(path, handler);
        return this;
    }

    public RequestHandler getNotFoundRequestHandler() {
        return notFoundRequestHandler;
    }

    public void setNotFoundRequestHandler(RequestHandler notFoundRequestHandler) {
        this.notFoundRequestHandler = notFoundRequestHandler;
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
