package top.wuare.json.exception;

/**
 * common exception
 *
 * @author wuare
 * @since 2021/6/15
 */
public class CommonException extends RuntimeException {
    
    public CommonException(String message) {
        super(message);
    }

    public CommonException(Throwable cause) {
        super(cause);
    }
}
