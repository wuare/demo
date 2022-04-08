package top.wuare.json;

import org.junit.Assert;
import org.junit.Test;
import top.wuare.json.data.*;
import top.wuare.json.exception.JsonConvertException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WsonTest {

    @Test
    public void test0() {
        String text = "{\"id\":1,\"name\":\"wuare\"}";
        Wson wson = new Wson();
        User user = wson.fromJson(text, User.class);
        System.out.println(user);
    }

    @Test
    public void testToInt() {
        String text = "1";
        int val = new Wson().fromJson(text, int.class);
        Assert.assertEquals(val, 1);
    }

    @Test
    public void testToInteger() {
        String text = "1";
        Integer integer = new Wson().fromJson(text, Integer.class);
        Assert.assertEquals(integer, new Integer(1));
    }

    @Test
    public void testArray() {
        String text = "{\"ids\": [1,2,3]}";
        ArrayData data = new Wson().fromJson(text, ArrayData.class);
        Assert.assertEquals(data.getIds().length, 3);
    }

    @Test
    public void testString() {
        String text = "{\"name\": \"str\"}";
        StringData data = new Wson().fromJson(text, StringData.class);
        Assert.assertEquals(data.getName(), "str");
    }

    @Test
    public void testMapGeneric() {
        String text = "{\"map\":{\"z\":{\"a\":[1]}}}";
        MapGenericData mapGenericData = new Wson().fromJson(text, MapGenericData.class);
        Map<String, List<Long>> z = mapGenericData.getMap().get("z");
        Assert.assertNotNull(z);
        List<Long> a = z.get("a");
        Assert.assertNotNull(a);
        Assert.assertEquals(a.size(), 1);
        Assert.assertEquals(a.get(0).longValue(), 1L);
    }

    @Test
    public void testMapGenericText() {
        MapGenericData data = new MapGenericData();
        Map<String, Map<String, List<Long>>> map = new HashMap<>();
        Map<String, List<Long>> map0 = new HashMap<>();
        List<Long> list = new ArrayList<>();
        list.add(1L);
        map0.put("a", list);
        map.put("z", map0);
        data.setMap(map);
        System.out.println(new Wson().toJson(data));
    }

    @Test
    public void testAllData() {
        String text = writeAllDataToJson();
        AllData data = new Wson().fromJson(text, AllData.class);
        Assert.assertEquals(data.getS(), "allDataName");
        Assert.assertEquals(data.getI0(), 1);
        Assert.assertEquals(data.getI1(), Integer.valueOf(2));
        Assert.assertEquals(data.getL0(), 1);
        Assert.assertEquals(data.getL1(), Long.valueOf(2));
        Assert.assertEquals(data.getF0(), 1.1, 0.000001);
        Assert.assertEquals(data.getF1(), Float.valueOf(1.2F));
        Assert.assertEquals(data.getD0(), 1.1D, 0.000001D);
        Assert.assertEquals(data.getD1(), Double.valueOf(1.2D));
        Assert.assertEquals(data.getUser().getId(), 10);
        Assert.assertEquals(data.getUser().getName(), "u");
        Assert.assertEquals(data.getUsers().length, 2);
        Assert.assertEquals(data.getUsers()[0].getId(), 123);
        Assert.assertEquals(data.getUsers()[0].getName(), "u123");
        Assert.assertEquals(data.getUsers()[1].getId(), 456);
        Assert.assertEquals(data.getUsers()[1].getName(), "u456");
    }

    @Test(expected = JsonConvertException.class)
    public void testOther() {
        String text = "{\"data\":[1,2,3]}";
        new Wson().fromJson(text, SetData.class);
    }

    @Test
    public void testSetData() {

    }

    @Test
    public void writeJson() {
        ArrayData data = new ArrayData();
        data.setIds(new int[]{1, 2, 3});
        System.out.println(new Wson().toJson(data));
    }

    private String writeAllDataToJson() {
        AllData data = new AllData();
        data.setS("allDataName");
        data.setI0(1);
        data.setI1(2);
        data.setL0(1);
        data.setL1(2L);
        data.setF0(1.1F);
        data.setF1(1.2F);
        data.setD0(1.1D);
        data.setD1(1.2D);
        User user = new User();
        user.setId(10);
        user.setName("u");
        data.setUser(user);

        User user0 = new User();
        user0.setId(123);
        user0.setName("u123");
        User user1 = new User();
        user1.setId(456);
        user1.setName("u456");
        data.setUsers(new User[]{user0, user1});
        return new Wson().toJson(data);
    }

    @Test(expected = JsonConvertException.class)
    public void CyclicDependence() {
        User user = new User();
        user.setId(1);
        UserAttach attach = new UserAttach();
        attach.setUser(user);
        user.setAttach(attach);
        String s = new Wson().toJson(user);
        System.out.println(s);
    }
}
