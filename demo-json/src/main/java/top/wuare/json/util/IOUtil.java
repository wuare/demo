package top.wuare.json.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * util for io
 *
 * @author wuare
 * @since 2021/6/15
 */
public class IOUtil {

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ignored) {
        }
    }

}
