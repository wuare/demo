package top.wuare.http.exception;

/**
 * custom exception
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpServerException extends RuntimeException {
    public HttpServerException(String message) {
        super(message);
    }

    public HttpServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpServerException(Throwable cause) {
        super(cause);
    }
}
