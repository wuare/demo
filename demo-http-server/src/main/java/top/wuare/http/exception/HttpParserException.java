package top.wuare.http.exception;

/**
 * http parser exception
 *
 * @author wuare
 * @date 2021/6/21
 */
public class HttpParserException extends RuntimeException {

    public HttpParserException(String message) {
        super(message);
    }

    public HttpParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpParserException(Throwable cause) {
        super(cause);
    }
}
