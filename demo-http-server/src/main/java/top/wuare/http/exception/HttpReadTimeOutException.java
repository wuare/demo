package top.wuare.http.exception;

public class HttpReadTimeOutException extends RuntimeException {

    public HttpReadTimeOutException(String message) {
        super(message);
    }

    public HttpReadTimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public HttpReadTimeOutException(Throwable cause) {
        super(cause);
    }
}
