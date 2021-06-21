package top.wuare.http;

import top.wuare.http.exception.HttpServerException;
import top.wuare.http.handler.DefaultHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
    }

    public void start() {
        init();
        logger.info("the thread pool config, coreSize: {" + coreSize + "}, maxSize: {" + maxSize + "}.");
        while (isRunning()) {
            try {
                Socket socket = serverSocket.accept();
                executorService.execute(new DefaultHandler(socket));
            } catch (IOException e) {
                logger.severe(e.getMessage());
            }
        }
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
}
