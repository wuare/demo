package top.wuare.json;

import top.wuare.json.convert.JsonConvert;
import top.wuare.json.parser.JsonParser;

public class Wson {

    private final JsonParser parser = new JsonParser();
    private final JsonConvert convert = new JsonConvert();

    public Object fromJson(String text) {
        return parser.parse(text);
    }

    public String toJson(Object obj) {
        return convert.toJson(obj);
    }
}
