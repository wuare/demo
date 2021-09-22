package top.wuare.json.exception;

public class JsonConvertException extends RuntimeException {

    public JsonConvertException(String message) {
        super(message);
    }

    public JsonConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
