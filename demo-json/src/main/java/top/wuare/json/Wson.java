package top.wuare.json;

import top.wuare.json.convert.JsonConvert;
import top.wuare.json.parser.JsonParser;

public class Wson {

    private final JsonConvert convert = new JsonConvert();

    public Object fromJson(String text) {
        return new JsonParser().parse(text);
    }

    public <T> T fromJson(String text, Class<T> tClass) {
        return convert.fromJson(new JsonParser().parse(text), tClass);
    }

    public String toJson(Object obj) {
        return convert.toJson(obj);
    }
}
