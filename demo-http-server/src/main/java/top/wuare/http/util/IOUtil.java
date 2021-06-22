package top.wuare.http.util;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * io utils
 *
 * @author wuare
 * @date 2021/6/22
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
