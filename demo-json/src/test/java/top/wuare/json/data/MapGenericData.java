package top.wuare.json.data;

import java.util.List;
import java.util.Map;

public class MapGenericData {

    private Map<String, Map<String, List<Long>>> map;

    public Map<String, Map<String, List<Long>>> getMap() {
        return map;
    }

    public void setMap(Map<String, Map<String, List<Long>>> map) {
        this.map = map;
    }
}
