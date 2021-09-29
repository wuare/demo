package top.wuare.json.convert;

import org.junit.Test;
import top.wuare.json.data.MapData;
import top.wuare.json.parser.JsonParser;

import java.util.LinkedHashMap;

/**
 * test convert
 *
 * @author wuare
 * @date 2021/6/18
 */
public class JsonConvertTest {

    private final JsonConvert convert = new JsonConvert();
    private final JsonParser parser = new JsonParser();

    @Test
    public void testConvertBean() {

    }

    @Test
    public void testConvertBean1() {

    }

    @Test
    public void testJsonToMap() {
        String json = "{\"map\":{\"a\":1,\"b\":2}}";
        MapData data = convert.fromJson(parser.parse(json), MapData.class);
        System.out.println(data);
    }

    @Test
    public void testConvertMapToJson() {
        MapData data = new MapData();
        LinkedHashMap<String, Long> map = new LinkedHashMap<>();
        map.put("a", 1L);
        map.put("b", 2L);
        data.setMap(map);
        System.out.println(convert.toJson(data));
    }
}
