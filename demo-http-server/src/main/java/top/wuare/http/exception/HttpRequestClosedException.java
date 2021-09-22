package top.wuare.http.exception;

public class HttpRequestClosedException extends RuntimeException {

    public HttpRequestClosedException(String message) {
        super(message);
    }

    public HttpRequestClosedException(Throwable cause) {
        super(cause);
    }
}
