package top.wuareb.highlight.gen.html;

import org.junit.Test;
import top.wuareb.highlight.gen.html.json.JsonGen;

public class JsonGenTest {

    @Test
    public void testGen() {
        String c = "{\"name\": \"wuare\", \"age\": 28, \"fat\": false, \"other\": null}";
        JsonGen gen = new JsonGen();
        String r = gen.gen(c);
        System.out.println(r);
    }
}
