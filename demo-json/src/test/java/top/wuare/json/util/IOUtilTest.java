package top.wuare.json.util;

import org.junit.Test;

import java.io.StringReader;

/**
 * IOUtil test
 */
public class IOUtilTest {

    @Test
    public void testClose() {
        StringReader reader = new StringReader("x");
        IOUtil.close(reader);
    }
}
