package top.wuare.json.util;

/**
 * util for io
 *
 * @author wuare
 * @since 2021/6/15
 */
public class IOUtil {

    public static void close(AutoCloseable... acs) {
        if (acs == null) {
            return;
        }
        for (AutoCloseable ac : acs) {
            try {
                if (ac != null) {
                    ac.close();
                }
            } catch (Exception ignored) {
            }
        }
    }

}
