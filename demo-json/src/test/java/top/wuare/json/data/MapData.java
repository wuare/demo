package top.wuare.json.data;

import java.util.LinkedHashMap;

public class MapData {

    private LinkedHashMap<String, Long> map;

    public LinkedHashMap<String, Long> getMap() {
        return map;
    }

    public void setMap(LinkedHashMap<String, Long> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "MapData{" +
                "map=" + map +
                '}';
    }
}
