package top.wuare.json.exception;

/**
 * common exception
 *
 * @author wuare
 * @since 2021/6/15
 */
public class CommonException extends RuntimeException {
    public CommonException() {
        super();
    }

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }
}
