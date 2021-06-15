package top.wuare.json.parser;

import org.junit.Test;

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
    public void testParser0() {
        String t = "";
        JsonParser parser = new JsonParser();
        Object obj = parser.parse(t);
        System.out.println(obj);
    }
}
