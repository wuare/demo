package top.wuare.json;

import top.wuare.json.convert.JsonConvert;
import top.wuare.json.parser.JsonParser;

public class Wson {


    public Object fromJson(String text) {
        return new JsonParser().parse(text);
    }

    public <T> T fromJson(String text, Class<T> tClass) {
        return new JsonConvert().fromJson(new JsonParser().parse(text), tClass);
    }

    public String toJson(Object obj) {
        return new JsonConvert().toJson(obj, true);
    }

    public String toString(Object obj) {
        return new JsonConvert().toJson(obj, false);
    }
}
