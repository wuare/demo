package top.wuare.json.parser;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * test JsonParser
 *
 * @author wuare
 * @date 2021/6/15
 */
public class JsonParserTest {

    @Test
    public void testParser() {
        String text = "{ \"orderCode\": \"C001\", \"type\": 1, \"pageNo\": 3 , \"amount\": -1.236 }";
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(text);
        System.out.println(obj);
    }

    @Test
    public void testParser0() throws Exception {
        File file = new File("E:/order.json");
        if (!file.exists()) {
            return;
        }
        if (!file.isFile()) {
            return;
        }
        FileInputStream in = new FileInputStream(file);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            byte[] buf = new byte[4096];
            int len;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
        } finally {
            try {
                in.close();
            } catch (Exception ignored) {}
        }

        String t = out.toString();
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(t);
        System.out.println(obj);
    }
}
